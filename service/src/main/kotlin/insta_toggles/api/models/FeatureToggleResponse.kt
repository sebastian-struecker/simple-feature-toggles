package insta_toggles.api.models

data class FeatureToggleResponse(
    val id: Long, val key: String, val name: String, val description: String, val contexts: List<ContextApiModel>
)
