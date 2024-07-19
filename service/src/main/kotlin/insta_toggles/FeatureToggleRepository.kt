package insta_toggles

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

interface FeatureToggleRepository {

    fun getAll(): Multi<FeatureToggle>
    fun getAllActive(context: Context): Multi<FeatureToggle>
    fun getById(id: Long): Uni<FeatureToggle>
    fun getByKey(key: String): Uni<FeatureToggle>
    fun create(key: String, name: String, description: String): Uni<FeatureToggle>
    fun update(featureToggle: FeatureToggle): Uni<FeatureToggle>
    fun removeById(id: Long): Uni<Unit>
    fun removeAll(): Uni<Unit>

}
