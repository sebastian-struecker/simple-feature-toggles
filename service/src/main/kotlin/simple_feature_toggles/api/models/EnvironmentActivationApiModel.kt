package simple_feature_toggles.api.models

import com.fasterxml.jackson.annotation.JsonProperty

data class EnvironmentActivationApiModel(
    val environmentKey: String,
    @JsonProperty("activated")
    val activated: Boolean
)
