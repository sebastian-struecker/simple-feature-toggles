package insta_toggles

class Feature(
    val id: Long, var name: String, var description: String, var isActive: Boolean
) {
    init {
        require(name.isNotBlank()) {
            "Feature name must not be blank"
        }
    }
}
