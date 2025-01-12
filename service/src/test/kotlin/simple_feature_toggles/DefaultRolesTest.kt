package simple_feature_toggles

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DefaultRolesTest {

    @Test
    fun default_roles_test() {
        assertEquals(listOf("sft_admin", "sft_viewer"), DefaultRoles.getAll())
    }
}
