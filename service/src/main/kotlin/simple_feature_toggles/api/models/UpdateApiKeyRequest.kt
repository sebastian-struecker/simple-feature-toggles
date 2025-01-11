package simple_feature_toggles.api.models

data class UpdateApiKeyRequest(
    val name: String? = null, val environmentActivations: List<EnvironmentActivationApiModel>? = null
)
