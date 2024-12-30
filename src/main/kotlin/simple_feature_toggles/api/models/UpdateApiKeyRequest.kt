package simple_feature_toggles.api.models

data class UpdateApiKeyRequest(
    val name: String? = null, val environmentActivation: Map<String, Boolean>? = null
)
