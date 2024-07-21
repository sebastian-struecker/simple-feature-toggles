package insta_toggles.api

import io.quarkus.security.UnauthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.server.ServerRequestFilter

class ApiKeySecurityFilter(@ConfigProperty(name = "api-keys") val apiKeys: List<String>) {

    @ServerRequestFilter(preMatching = true)
    fun filterApiKey(containerRequestContext: ContainerRequestContext) {
        val path = containerRequestContext.uriInfo.path
        if (!path.equals("/feature-toggles/testing") && !path.equals("/feature-toggles/production")) {
            return
        }
        val apiKeyHeader = containerRequestContext.getHeaderString("x-api-key")
        if (!apiKeys.contains(apiKeyHeader)) {
            throw UnauthorizedException("Api key is missing")
        }
    }

}
