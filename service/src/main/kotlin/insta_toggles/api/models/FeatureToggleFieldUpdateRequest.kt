package insta_toggles.api.models

import insta_toggles.Context

data class FeatureToggleFieldUpdateRequest(
    var name: String?, var description: String?, var activation: Map<Context, Boolean>?
)
