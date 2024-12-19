package simple_feature_toggles.api

import io.quarkus.logging.Log
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.resteasy.reactive.NoCache
import org.jboss.resteasy.reactive.RestResponse
import simple_feature_toggles.Context
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.FeatureToggleRepository
import simple_feature_toggles.api.models.ContextApiModel
import simple_feature_toggles.api.models.CreateFeatureToggleRequest
import simple_feature_toggles.api.models.FeatureToggleResponse
import simple_feature_toggles.api.models.FeatureToggleUpdateRequest

@ApplicationScoped
@Path("feature-toggles")
@Tag(name = "Feature Toggle API", description = "API for managing feature toggles")
@SecurityScheme(
    securitySchemeName = "JWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"
)
class ManagementApi(
    val featureToggleRepository: FeatureToggleRepository
) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getAll", summary = "Get all feature toggles")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A list of all feature toggles", content = [Content(
                mediaType = "application/json",
                schema = Schema(type = SchemaType.ARRAY, implementation = FeatureToggleResponse::class)
            )]
        )]
    )
    fun getAll(): Multi<FeatureToggleResponse> {
        Log.debug("[FeatureToggleApi] Calling method: get url: /feature-toggles")
        return featureToggleRepository.getAll().map { it.toResponse() }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getById", summary = "Get a feature toggle by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A single feature toggle", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = FeatureToggleResponse::class)
            )]
        ), APIResponse(responseCode = "404", description = "Feature toggle not found")]
    )
    fun getById(id: Long): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: get url: /feature-toggles/$id")
        return featureToggleRepository.getById(id).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "create", summary = "Create a new feature toggle")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "Feature toggle created", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = FeatureToggleResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input")]
    )
    fun create(request: CreateFeatureToggleRequest): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: post url: /feature-toggles body: $request")
        return featureToggleRepository.create(request.key, request.name, request.description).onItem()
            .transform { RestResponse.ok(it.toResponse()) }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "partialUpdate", summary = "Partially update a feature toggle by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "Feature toggle updated", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = FeatureToggleResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input"), APIResponse(
            responseCode = "404", description = "Feature toggle not found"
        )]
    )
    fun partialUpdate(
        id: Long, updates: FeatureToggleUpdateRequest
    ): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: patch url: /feature-toggles/$id body: $updates")
        return featureToggleRepository.update(id, updates).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteById", summary = "Delete a feature toggle by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "Feature toggle deleted"
        ), APIResponse(responseCode = "404", description = "Feature toggle not found")]
    )
    fun deleteById(id: Long): Uni<RestResponse<Unit>> {
        Log.debug("[FeatureToggleApi] Calling method: delete url: /feature-toggles/$id")
        return featureToggleRepository.removeById(id).onItem().transform {
            RestResponse.ok(it)
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteAll", summary = "Delete all feature toggles")
    @APIResponses(
        value = [APIResponse(responseCode = "200", description = "All feature toggles deleted")]
    )
    fun deleteAll(): Uni<RestResponse<Unit>> {
        Log.debug("[FeatureToggleApi] Calling method: delete url: /feature-toggles")
        return featureToggleRepository.removeAll().onItem().transform {
            RestResponse.ok(it)
        }.onFailure().transform { NotFoundException() }
    }

    fun FeatureToggle.toResponse(): FeatureToggleResponse {
        return FeatureToggleResponse(id, key, name, description, contexts.map { it.toResponse() })
    }

    fun Context.toResponse(): ContextApiModel {
        return ContextApiModel(key, isActive)
    }
}
