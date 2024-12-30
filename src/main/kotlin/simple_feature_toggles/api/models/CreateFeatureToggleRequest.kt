package simple_feature_toggles.api.models

data class CreateFeatureToggleRequest(
    val key: String,
    val name: String,
    val description: String,
    val environmentActivation: Map<String, Boolean> = mapOf()
)
