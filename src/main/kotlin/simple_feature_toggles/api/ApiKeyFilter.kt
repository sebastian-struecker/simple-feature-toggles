package simple_feature_toggles.api

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import org.jboss.resteasy.reactive.server.ServerRequestFilter
import simple_feature_toggles.ApiKeyRepository
import java.time.Duration

private const val INVALID_API_KEY = "Invalid Api-Key"

@ApplicationScoped
class ApiKeyFilter(
    val apiKeyRepository: ApiKeyRepository
) {

    companion object {
        const val apiKeyHeaderName = "x-api-key"
    }

    @ServerRequestFilter(preMatching = true)
    fun filter(requestContext: ContainerRequestContext): Uni<Void> {
        val apiKeyHeader = requestContext.getHeaderString(apiKeyHeaderName)
        val path = requestContext.uriInfo.path
        val queryParameters: MutableList<String>? = requestContext.uriInfo.queryParameters["environment"]
        if (!isApiKeySecurePath(path)) {
            return Uni.createFrom().voidItem()
        }
        if (queryParameters?.isEmpty() == true || queryParameters?.first() == null) {
            return Uni.createFrom().failure(NotAuthorizedException(INVALID_API_KEY))
        }
        if (apiKeyHeader == null) {
            return Uni.createFrom().failure(NotAuthorizedException(INVALID_API_KEY))
        }
        val env: String = queryParameters.first()
        return apiKeyRepository.getAll().onItem().transformToUni { apiKeys ->
            var isApiKeyCorrect = false
            apiKeys.forEach {
                if (it.environmentActivation[env] == true) {
                    isApiKeyCorrect = true
                }
            }
            if (isApiKeyCorrect) {
                Uni.createFrom().voidItem()
            } else {
                Uni.createFrom().failure(NotAuthorizedException(INVALID_API_KEY))
            }
        }.ifNoItem().after(Duration.ofMillis(100)).failWith(NotAuthorizedException(INVALID_API_KEY))
    }

    private fun isApiKeySecurePath(path: String): Boolean {
        return path.contains("client/feature-toggles")
    }

}
