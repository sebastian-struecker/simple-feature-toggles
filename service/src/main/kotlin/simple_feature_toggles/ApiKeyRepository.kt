package simple_feature_toggles

import io.smallrye.mutiny.Uni
import simple_feature_toggles.api.models.CreateApiKeyRequest
import simple_feature_toggles.api.models.UpdateApiKeyRequest

interface ApiKeyRepository {

    fun getAll(): Uni<List<ApiKey>>

    fun getById(id: Long): Uni<ApiKey>

    fun create(createRequest: CreateApiKeyRequest): Uni<ApiKey>

    fun update(id: Long, updates: UpdateApiKeyRequest): Uni<ApiKey>

    fun removeById(id: Long): Uni<Unit>

    fun removeAll(): Uni<Unit>

    fun onEnvironmentRemoval(environmentKey: String): Uni<String>

}
