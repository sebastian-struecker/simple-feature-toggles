package simple_feature_toggles

object DefaultRoles {
    const val ADMIN: String = "admin"
    const val VIEWER: String = "viewer"
    const val RELEASE_MANAGER: String = "release_manager"

    fun getAll(): List<String> {
        return listOf(ADMIN, VIEWER, RELEASE_MANAGER)
    }

    fun isDefaultRole(role: String): Boolean {
        when (role) {
            ADMIN -> return true
            VIEWER -> return true
            RELEASE_MANAGER -> return true
            else -> return false
        }
    }
}
