package insta_toggles.api.models

data class FeatureResponse(
    val id: Long, val name: String, val description: String, val isActive: Boolean
)
