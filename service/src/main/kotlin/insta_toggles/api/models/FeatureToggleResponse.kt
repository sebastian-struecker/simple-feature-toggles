package insta_toggles.api.models

import insta_toggles.Context

data class FeatureToggleResponse(
    val id: Long,
    val key: String,
    val name: String,
    val description: String,
    val activationMap: Map<Context, Boolean>
)
