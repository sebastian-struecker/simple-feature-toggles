package simple_feature_toggles

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FeatureToggleTest {

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            FeatureToggle(1, "key", "name", "description", mutableMapOf())
        }
    }

    @Test
    fun validate_key_pattern_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(1, "123456", "name", "description", mutableMapOf())
            FeatureToggle(1, "abc_", "name", "description", mutableMapOf())
            FeatureToggle(1, "abc_12", "name", "description", mutableMapOf())

        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            FeatureToggle(1, "key", "", "description", mutableMapOf())
        }
    }

    @Test
    fun check_activation_test() {
        val featureToggle = featureToggle()
        featureToggle.environmentActivation.forEach { (_, value) ->
            Assertions.assertTrue(value)
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
            featureToggle() == featureToggle(environmentActivation = mutableMapOf())
        })
    }

    @Test
    fun hashCode_test() {
        Assertions.assertTrue({
            featureToggle().hashCode() == featureToggle().hashCode()
        })
    }

    private fun featureToggle(
        id: Long = 1,
        key: String = "key",
        name: String = "name",
        description: String = "description",
        environmentActivation: MutableMap<String, Boolean> = environmentActivation()
    ) = FeatureToggle(id, key, name, description, environmentActivation)

    private fun environmentActivation() = mutableMapOf(
        "dev" to true, "prod" to true
    )
}
