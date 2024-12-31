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
import simple_feature_toggles.ApiKey
import simple_feature_toggles.ApiKeyRepository
import simple_feature_toggles.DefaultRoles
import simple_feature_toggles.api.models.ApiKeyResponse
import simple_feature_toggles.api.models.CreateApiKeyRequest
import simple_feature_toggles.api.models.FeatureToggleResponse
import simple_feature_toggles.api.models.UpdateApiKeyRequest

@ApplicationScoped
@Path("api-keys")
@Tag(name = "Api Key API", description = "API for managing api keys")
@SecurityScheme(
    securitySchemeName = "JWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"
)
class ApiKeyApi(
    val repository: ApiKeyRepository
) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getAll", summary = "Get all api keys")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A list of all api keys", content = [Content(
                mediaType = "application/json",
                schema = Schema(type = SchemaType.ARRAY, implementation = ApiKeyResponse::class)
            )]
        )]
    )
    fun getAllApiKeys(): Multi<ApiKeyResponse> {
        Log.debug("[ApiKeyApi] Calling method: get url: /api-keys")
        return repository.getAll().toMulti().flatMap { list -> Multi.createFrom().iterable(list) }
            .map { it.toResponse() }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "getById", summary = "Get an api key by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A single api key", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = ApiKeyResponse::class)
            )]
        ), APIResponse(responseCode = "404", description = "api key not found")]
    )
    fun getApiKeyById(id: Long): Uni<RestResponse<ApiKeyResponse>> {
        Log.debug("[ApiKeyApi] Calling method: get url: /api-keys/$id")
        return repository.getById(id).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "create", summary = "Create a new api key")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "api key created", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = ApiKeyResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input")]
    )
    fun createApiKey(request: CreateApiKeyRequest): Uni<RestResponse<ApiKeyResponse>> {
        Log.debug("[ApiKeyApi] Calling method: post url: /api-keys body: $request")
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
    @Operation(operationId = "partialUpdate", summary = "Partially update an api key by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "api key updated", content = [Content(
                mediaType = "application/json", schema = Schema(implementation = FeatureToggleResponse::class)
            )]
        ), APIResponse(responseCode = "400", description = "Invalid input"), APIResponse(
            responseCode = "404", description = "api key not found"
        )]
    )
    fun partialApiKeyUpdate(
        id: Long, updates: UpdateApiKeyRequest
    ): Uni<RestResponse<ApiKeyResponse>> {
        Log.debug("[ApiKeyApi] Calling method: patch url: /api-keys/$id body: $updates")
        return repository.update(id, updates).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteById", summary = "Delete an api key by ID")
    @APIResponses(
        value = [APIResponse(
            responseCode = "204", description = "api key deleted"
        ), APIResponse(responseCode = "404", description = "api key not found")]
    )
    fun deleteApiKeyById(id: Long): Uni<RestResponse<Unit>> {
        Log.debug("[ApiKeyApi] Calling method: delete url: /api-keys/$id")
        return repository.removeById(id).onItem().transform {
            RestResponse.noContent<Unit>()
        }.onFailure().transform { NotFoundException() }
    }

    @DELETE
    @RolesAllowed(DefaultRoles.ADMIN)
    @SecurityRequirement(name = "JWT")
    @Operation(operationId = "deleteAll", summary = "Delete all api keys")
    @APIResponses(
        value = [APIResponse(responseCode = "204", description = "All api keys deleted")]
    )
    fun deleteAllApiKeys(): Uni<RestResponse<Unit>> {
        Log.debug("[ApiKeyApi] Calling method: delete url: /api-keys")
        return repository.removeAll().onItem().transform {
            RestResponse.noContent<Unit>()
        }.onFailure().transform { NotFoundException() }
    }

    fun ApiKey.toResponse(): ApiKeyResponse {
        return ApiKeyResponse(
            id = id,
            name = name,
            secret = secret,
            environmentActivation = environmentActivation.map { (key, value) -> key to value }.toMap()
        )
    }
}
