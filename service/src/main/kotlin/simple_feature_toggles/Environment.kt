package simple_feature_toggles

class Environment(
    val id: Long, val key: String, var name: String
) {
    init {
        checkInputs(key, name)
    }

    companion object {
        fun checkInputs(key: String, name: String) {
            require(key.matches(Regex("^[a-z_]*[a-z]$"))) {
                "Environment key '$key' is invalid"
            }
            require(name.isNotBlank()) {
                "Environment name must not be blank"
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Environment

        if (id != other.id) return false
        if (key != other.key) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
