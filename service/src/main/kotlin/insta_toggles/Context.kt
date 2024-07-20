package insta_toggles

class Context(
    val id: Long, val key: String, var name: String, var isActive: Boolean
) {
    init {
        require(key.matches(Regex("^[a-z_]*[a-z]$"))) {
            "Feature key '$key' is invalid"
        }
        require(key == ContextName.testing.toString() || key == ContextName.production.toString()) {
            "Feature key '$key' does not match testing or production"
        }
        require(name.isNotBlank()) {
            "Feature name must not be blank"
        }
        require(name == ContextName.testing.toString() || name == ContextName.production.toString()) {
            "Feature name '$name' does not match testing or production"
        }
    }
}
