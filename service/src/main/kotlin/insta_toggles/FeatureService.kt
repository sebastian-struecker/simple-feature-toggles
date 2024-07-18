package insta_toggles

import insta_toggles.api.models.PartialFeatureUpdateRequest
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeatureService(val featureRepository: FeatureRepository) {

    fun create(name: String, description: String): Uni<Feature> {
        return featureRepository.create(name, description)
    }

    fun update(id: Long, updates: PartialFeatureUpdateRequest): Uni<Feature> {
        return featureRepository.getById(id).onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found")).onItem().ifNotNull()
            .transformToUni { it: Feature? ->
                it!!.apply {
                    if (updates.name?.isNotBlank() == true) {
                        name = updates.name!!
                    }
                    if (updates.description?.isNotBlank() == true) {
                        description = updates.description!!
                    }
                    if (updates.isActive != null) {
                        isActive = updates.isActive!!
                    }
                }
                featureRepository.update(it)
            }
    }

}
