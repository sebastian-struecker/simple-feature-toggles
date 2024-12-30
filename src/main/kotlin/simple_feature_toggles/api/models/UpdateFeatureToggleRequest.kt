package simple_feature_toggles.api.models

data class UpdateFeatureToggleRequest(
    val name: String? = null, val description: String? = null, val environmentActivation: Map<String, Boolean>? = null
)
