package insta_toggles.api.models

data class PartialFeatureUpdateRequest(
    var name: String?, var description: String?, var isActive: Boolean?
)
