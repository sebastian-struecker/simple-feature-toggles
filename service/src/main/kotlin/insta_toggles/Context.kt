package insta_toggles

class Context(
    val id: Long, val key: String, var name: String, var isActive: Boolean
) {
    init {
        require(key == ContextName.testing.toString() || key == ContextName.production.toString()) {
            "Feature key '$key' does not match testing or production"
        }
        require(name == key) {
            "Feature name '$name' does not match key '$key'"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Context

        if (id != other.id) return false
        if (key != other.key) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }

}
