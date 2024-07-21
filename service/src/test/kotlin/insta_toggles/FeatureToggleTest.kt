package insta_toggles

import org.junit.jupiter.api.Assertions
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

    @Test
    fun equals_test() {
        val featureToggle = featureToggle()
        Assertions.assertTrue({
            featureToggle == featureToggle
            featureToggle() == featureToggle()
        })
        Assertions.assertFalse({
            featureToggle(id = 1) == featureToggle(id = 2)
            featureToggle(key = "one") == featureToggle(key = "two")
            featureToggle(name = "one") == featureToggle(name = "two")
            featureToggle(description = "one") == featureToggle(description = "two")
        })
    }

    @Test
    fun hashCode_test() {
        Assertions.assertTrue({
            featureToggle().hashCode() == featureToggle().hashCode()
        })
    }

    private fun featureToggle(
        id: Long = 1, key: String = "key", name: String = "name", description: String = "description"
    ) = FeatureToggle(id, key, name, description, contexts())

    private fun contexts() = listOf(
        Context(1, ContextName.testing.toString(), ContextName.testing.toString(), false),
        Context(1, ContextName.production.toString(), ContextName.production.toString(), false)
    )
}
