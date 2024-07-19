package insta_toggles

class FeatureToggle(
    val id: Long,
    val key: String,
    var name: String,
    var description: String,
    val activation: MutableMap<Context, Boolean>
) {
    init {
        require(key.matches(Regex("^[a-z_]*[a-z]$"))) {
            "Feature key '$key' is invalid"
        }
        require(name.isNotBlank()) {
            "Feature name must not be blank"
        }
    }
}
