package insta_toggles

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FeatureToggleTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            FeatureToggle(1, "key", "name", "description", contexts())
        }
    }

    @Test
    fun validate_key_pattern_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(1, "123456", "name", "description", contexts())
            FeatureToggle(1, "abc_", "name", "description", contexts())
            FeatureToggle(1, "abc_12", "name", "description", contexts())

        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(1, "key", "", "description", contexts())
        }
    }

    private fun contexts() = listOf(
        Context(1, ContextName.testing.toString(), ContextName.testing.toString(), false),
        Context(1, ContextName.production.toString(), ContextName.production.toString(), false)
    )
}
