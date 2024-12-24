package simple_feature_toggles

import simple_feature_toggles.api.models.UpdateFeatureToggleRequest
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

interface FeatureToggleRepository {

    fun getAll(): Multi<FeatureToggle>
    fun getAllActive(contextName: ContextName): Multi<FeatureToggle>
    fun getById(id: Long): Uni<FeatureToggle>
    fun getByKey(key: String): Uni<FeatureToggle>
    fun create(key: String, name: String, description: String): Uni<FeatureToggle>
    fun update(id: Long, updates: UpdateFeatureToggleRequest): Uni<FeatureToggle>
    fun removeById(id: Long): Uni<Unit>
    fun removeAll(): Uni<Unit>

}
