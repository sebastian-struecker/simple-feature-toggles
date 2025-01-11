package simple_feature_toggles.api.models

data class CreateEnvironmentRequest(
    val key: String,
    val name: String
)
