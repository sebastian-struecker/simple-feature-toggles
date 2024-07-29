package simple_feature_toggles

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ContextTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            Context(1, "testing", "testing", false)
            Context(2, "production", "production", false)
        }
    }

    @Test
    fun instance_keyNotTestingOrProduction_failure_test() {
        assertThrows<IllegalArgumentException> {
            Context(1, "fail", "testing", false)
        }
    }

    @Test
    fun instance_keyNotName_failure_test() {
        assertThrows<IllegalArgumentException> {
            Context(1, "testing", "production", false)
            Context(1, "production", "testing", false)
        }
    }

    @Test
    fun equals_test() {
        val context = context()
        Assertions.assertTrue({
            context == context
            context() == context()
        })
        Assertions.assertFalse({
            context(id = 1) == context(id = 2)
            context(key = "testing") == context(key = "production")
            context(isActive = true) == context(isActive = false)
        })
    }

    @Test
    fun hashCode_test() {
        Assertions.assertTrue({
            context().hashCode() == context().hashCode()
        })
    }

    private fun context(id: Long = 1, key: String = "testing", isActive: Boolean = false) =
        Context(id, key, key, isActive)

}
