package simple_feature_toggles.api

import io.quarkus.logging.Log
import io.smallrye.mutiny.Multi
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.resteasy.reactive.NoCache
import simple_feature_toggles.ContextName
import simple_feature_toggles.FeatureToggleRepository

@ApplicationScoped
@Path("client")
@Tag(name = "Client API", description = "API for clients")
@SecurityScheme(
    securitySchemeName = "APIKEY",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    apiKeyName = "x-api-key"
)
class ClientApi(
    val featureToggleRepository: FeatureToggleRepository
) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @Path("feature-toggles/{context}")
    @SecurityRequirement(name = "APIKEY")
    @Operation(operationId = "getAllActiveFeaturesForContext", summary = "Get all active features for a given context")
    @APIResponses(
        value = [APIResponse(
            responseCode = "200", description = "A list of active feature names", content = [Content(
                mediaType = "application/json", schema = Schema(type = SchemaType.ARRAY, implementation = String::class)
            )]
        ), APIResponse(responseCode = "401", description = "Invalid api-key")]
    )
    fun getAllActiveFeaturesForContext(context: String): Multi<String> {
        Log.debug("[ClientApi] Calling method: get url: /feature-toggles/$context")
        try {
            val contextName: ContextName = ContextName.valueOf(context.lowercase())
            return featureToggleRepository.getAllActive(contextName).onItem().transform { it.name }
        } catch (ex: IllegalArgumentException) {
            return Multi.createFrom().empty()
        }
    }
}
