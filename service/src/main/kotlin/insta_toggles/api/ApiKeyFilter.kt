package insta_toggles.api

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.server.ServerRequestFilter

@ApplicationScoped
class ApiKeyFilter(
    @ConfigProperty(name = "api.key.values")
    val apiKeyValues: List<String>,
    @ConfigProperty(name = "api.key.enable", defaultValue = "true")
    val apiKeyEnable: Boolean
) {

    companion object {
        const val apiKeyHeaderName = "x-api-key"
    }

    @ServerRequestFilter(preMatching = true)
    fun filter(requestContext: ContainerRequestContext) {
        if (!apiKeyEnable) return
        val path = requestContext.uriInfo.path
        if (isApiKeySecurePath(path)) {
            val apiKeyHeader = requestContext.getHeaderString(apiKeyHeaderName)
            if (apiKeyHeader == null || !apiKeyValues.contains(apiKeyHeader)) {
                throw NotAuthorizedException("Api-Key is missing")
            }
        }
    }

    private fun isApiKeySecurePath(path: String): Boolean {
        return path.contains("feature-toggles/testing") || path.contains("feature-toggles/production")
    }

}
