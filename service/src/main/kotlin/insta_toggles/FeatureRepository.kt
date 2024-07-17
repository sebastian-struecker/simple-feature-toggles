package insta_toggles

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

interface FeatureRepository {

    fun getAll(): Multi<Feature>
    fun getAllActive(): Multi<Feature>
    fun getById(id: Long): Uni<Feature?>
    fun getByName(name: String): Uni<Feature?>
    fun create(name: String, description: String): Uni<Feature>
    fun update(feature: Feature): Uni<Feature>
}
