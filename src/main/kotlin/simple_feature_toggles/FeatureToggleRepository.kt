package simple_feature_toggles

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import simple_feature_toggles.api.models.CreateFeatureToggleRequest
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest

interface FeatureToggleRepository {

    fun getAll(): Multi<FeatureToggle>

    fun getAllActive(environmentKey: String): Multi<FeatureToggle>

    fun getById(id: Long): Uni<FeatureToggle>

    fun getByKey(key: String): Uni<FeatureToggle>

    fun create(createRequest: CreateFeatureToggleRequest): Uni<FeatureToggle>

    fun update(id: Long, updates: UpdateFeatureToggleRequest): Uni<FeatureToggle>

    fun removeById(id: Long): Uni<Unit>

    fun removeAll(): Uni<Unit>

    fun onEnvironmentRemoval(environmentKey: String): Uni<String>

}
