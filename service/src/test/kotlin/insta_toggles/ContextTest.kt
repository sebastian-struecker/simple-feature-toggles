package insta_toggles

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ContextTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            Context(1, "testing", "testing", false)
        }
    }

    @Test
    fun validate_key_pattern_test() {
        assertThrows<IllegalArgumentException> {
            Context(1, "123456", "testing", false)
            Context(1, "abc_", "testing", false)
            Context(1, "abc_12", "testing", false)
        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            Context(1, "testing", "", false)
        }
    }

}
