package simple_feature_toggles.api.models

data class UpdateFeatureToggleRequest(
    var name: String? = null, var description: String? = null, var contexts: List<ContextApiModel>? = null
)
