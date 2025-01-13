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
import simple_feature_toggles.DefaultRoles
import simple_feature_toggles.Environment
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.EnvironmentApiResponse
import simple_feature_toggles.api.models.UpdateEnvironmentRequest

@ApplicationScoped
@Path("environments")
@Tag(name = "Environment API", description = "API for managing environments")
@SecurityScheme(
    securitySchemeName = "JWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"
)
class EnvironmentApi(
    val repository: EnvironmentRepository
) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.VIEWER)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getAll", summary = "Get all environments")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A list of all environments", content = [Content(
                mediaType = "application/json",
                schema = Schema(type = SchemaType.ARRAY, implementation = EnvironmentApiResponse::class)
            )]
        )]
    )
    fun getAllEnvironments(): Multi<EnvironmentApiResponse> {
        Log.debug("[EnvironmentApi] Calling method: get url: /environments")
        return repository.getAll().map { it.toResponse() }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.VIEWER)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getById", summary = "Get an environment by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A single environment", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = EnvironmentApiResponse::class)
            )]
        ), APIResponse(responseCode = "404", description = "environment not found")]
    )
    fun getEnvironmentById(id: Long): Uni<RestResponse<EnvironmentApiResponse>> {
        Log.debug("[EnvironmentApi] Calling method: get url: /environments/$id")
        return repository.getById(id).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "create", summary = "Create a new environment")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "environment created", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = EnvironmentApiResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input")]
    )
    fun createEnvironment(request: CreateEnvironmentRequest): Uni<RestResponse<EnvironmentApiResponse>> {
        Log.debug("[EnvironmentApi] Calling method: post url: /environments body: $request")
        try {
            return repository.create(request).onFailure().transform { BadRequestException() }.onItem()
                .transform { RestResponse.ok(it.toResponse()) }
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(e)
        }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "partialUpdate", summary = "Partially update an environment by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "environment updated", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = EnvironmentApiResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input"), APIResponse(
            responseCode = "404", description = "environment not found"
        )]
    )
    fun partialEnvironmentUpdate(
        id: Long, updates: UpdateEnvironmentRequest
    ): Uni<RestResponse<EnvironmentApiResponse>> {
        Log.debug("[EnvironmentApi] Calling method: patch url: /environments/$id body: $updates")
        return repository.update(id, updates).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteById", summary = "Delete an environment by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "204", description = "environment deleted"
        ), APIResponse(responseCode = "404", description = "environment not found")]
    )
    fun deleteEnvironmentById(id: Long): Uni<RestResponse<Unit>> {
        Log.debug("[EnvironmentApi] Calling method: delete url: /environments/$id")
        return repository.removeById(id).onItem().transform {
            RestResponse.noContent<Unit>()
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteAll", summary = "Delete all environments")
    @APIResponses(
        value = [APIResponse(responseCode = "204", description = "All environments deleted")]
    )
    fun deleteAllEnvironments(): Uni<RestResponse<Unit>> {
        Log.debug("[EnvironmentApi] Calling method: delete url: /environments")
        return repository.removeAll().onItem().transform {
            RestResponse.noContent<Unit>()
        }.onFailure().transform { NotFoundException() }
    }

    fun Environment.toResponse(): EnvironmentApiResponse {
        return EnvironmentApiResponse(
            id = id, key = key, name = name
        )
    }
}
