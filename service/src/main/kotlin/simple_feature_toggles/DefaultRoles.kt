package simple_feature_toggles

object DefaultRoles {
    const val ADMIN: String = "sft_admin"
    const val VIEWER: String = "sft_viewer"

    fun getAll(): List<String> {
        return listOf(ADMIN, VIEWER)
    }

    fun isDefaultRole(role: String): Boolean {
        when (role) {
            ADMIN -> return true
            VIEWER -> return true
            else -> return false
        }
    }
}
