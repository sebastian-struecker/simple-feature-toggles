package simple_feature_toggles

class FeatureToggle(
    val id: Long,
    val key: String,
    var name: String,
    var description: String,
    val environmentActivation: MutableMap<String, Boolean>
) {
    init {
        checkInputs(key, name)
    }

    companion object {
        fun checkInputs(key: String, name: String) {
            require(key.matches(Regex("^[a-z_]*[a-z]$"))) {
                "Feature key '$key' is invalid"
            }
            require(name.isNotBlank()) {
                "Feature name must not be blank"
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FeatureToggle

        if (id != other.id) return false
        if (key != other.key) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (environmentActivation != other.environmentActivation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + environmentActivation.hashCode()
        return result
    }
}
