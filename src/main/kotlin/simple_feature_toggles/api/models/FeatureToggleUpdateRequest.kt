package simple_feature_toggles.api.models

data class FeatureToggleUpdateRequest(
    var name: String? = null, var description: String? = null, var contexts: List<ContextApiModel>? = null
)
