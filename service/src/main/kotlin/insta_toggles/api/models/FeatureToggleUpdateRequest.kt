package insta_toggles.api.models

data class FeatureToggleUpdateRequest(
    var name: String?, var description: String?, var contexts: List<ContextApiModel>?
)
