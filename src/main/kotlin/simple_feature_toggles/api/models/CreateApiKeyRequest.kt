package simple_feature_toggles.api.models

data class CreateApiKeyRequest(
    val name: String,
    val environmentActivation: Map<String, Boolean> = mapOf()
)