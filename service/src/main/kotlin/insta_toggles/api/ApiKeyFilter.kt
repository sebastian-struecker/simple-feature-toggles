package insta_toggles.api

import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.server.ServerRequestFilter

class ApiKeyFilter(
    @ConfigProperty(name = "api-keys")
    val apiKeys: List<String>
) {

    companion object {
        const val apiKeyHeaderName = "x-api-key"
    }

    @ServerRequestFilter(preMatching = true)
    fun filter(requestContext: ContainerRequestContext) {
        val path = requestContext.uriInfo.path
        if (isApiKeySecurePath(path)) {
            val apiKeyHeader = requestContext.getHeaderString(apiKeyHeaderName)
            if (apiKeyHeader == null || !apiKeys.contains(apiKeyHeader)) {
                throw NotAuthorizedException("Api-Key is missing")
            }
        }
    }

    private fun isApiKeySecurePath(path: String): Boolean {
        return path.contains("feature-toggles/testing") || path.contains("feature-toggles/production")
    }

}
