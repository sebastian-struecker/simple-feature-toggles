package insta_toggles

import insta_toggles.api.models.FeatureToggleFieldUpdateRequest
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeatureToggleService(val featureToggleRepository: FeatureToggleRepository) {

    fun create(key: String, name: String, description: String): Uni<FeatureToggle> {
        return featureToggleRepository.create(key, name, description)
    }

    fun update(id: Long, updates: FeatureToggleFieldUpdateRequest): Uni<FeatureToggle> {
        return featureToggleRepository.getById(id).onItem().ifNotNull().transformToUni { it: FeatureToggle ->
            it.apply {
                if (updates.name?.isNotBlank() == true) {
                    name = updates.name!!
                }
                if (updates.description?.isNotBlank() == true) {
                    description = updates.description!!
                }
                if (updates.activation?.isNotEmpty() == true) {
                    updates.activation?.forEach { (contextKey, isActive) ->
                        activation.keys.firstOrNull { it == contextKey }?.let {
                            activation[it] = isActive
                        }
                    }
                }
            }
            featureToggleRepository.update(it)
        }
    }

}
