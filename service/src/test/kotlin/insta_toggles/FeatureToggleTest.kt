package insta_toggles

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FeatureToggleTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            FeatureToggle(
                1, "key", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
            )
        }
    }

    @Test
    fun validate_key_pattern_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(
                1, "123456", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
            )
        }
        assertThrows<IllegalArgumentException> {
            FeatureToggle(
                1, "abc_", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
            )
        }
        assertThrows<IllegalArgumentException> {
            FeatureToggle(
                1, "abc_12", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
            )
        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(
                1, "key", "", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
            )
        }
    }
}
