package simple_feature_toggles.api.models

data class ApiKeyResponse(
    val id: Long, val name: String, val value: String, val environmentActivation: Map<String, Boolean>
)
