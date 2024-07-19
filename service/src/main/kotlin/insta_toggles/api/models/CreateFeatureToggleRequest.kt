package insta_toggles.api.models

data class CreateFeatureToggleRequest(val key: String, val name: String, val description: String)
