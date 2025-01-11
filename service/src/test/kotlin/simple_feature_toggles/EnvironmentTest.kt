package simple_feature_toggles

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class EnvironmentTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            environment()
        }
    }

    @Test
    fun validate_key_pattern_test() {
        assertThrows<IllegalArgumentException> {
            environment(key = "123456")
            environment(key = "abc_")
            environment(key = "abc_12")
        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            environment(name = "")
        }
    }

    @Test
    fun equals_test() {
        val context = environment()
        Assertions.assertTrue({
            context == context
            environment() == environment()
        })
        Assertions.assertFalse({
            environment(id = 1) == environment(id = 2)
            environment(key = "testing") == environment(key = "production")
        })
    }

    @Test
    fun hashCode_test() {
        Assertions.assertTrue({
            environment().hashCode() == environment().hashCode()
        })
    }

    private fun environment(id: Long = 1, key: String = "key", name: String = "name") = Environment(id, key, name)

}
