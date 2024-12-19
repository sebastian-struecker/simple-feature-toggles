package simple_feature_toggles.api.models

data class ContextApiModel(
    val key: String, val isActive: Boolean
)
