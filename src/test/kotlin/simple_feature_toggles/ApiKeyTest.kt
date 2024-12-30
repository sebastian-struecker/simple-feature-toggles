package simple_feature_toggles

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ApiKeyTest {

    @Test
    fun generate_value_test() {
        val generateValue = ApiKey.generateValue()
        Assertions.assertTrue({
            generateValue.length == 24
        })
    }

    @Test
    fun create_instance_test() {
        assertDoesNotThrow {
            apiKey()
        }
    }

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> {
            apiKey(name = "")
        }
    }

    @Test
    fun check_activation_test() {
        val apiKey = apiKey()
        apiKey.environmentActivation.forEach { (_, value) ->
            Assertions.assertTrue(value)
        }
    }

    @Test
    fun equals_test() {
        val apiKey = apiKey()
        Assertions.assertTrue({
            apiKey == apiKey
            apiKey() == apiKey()
        })
        Assertions.assertFalse({
            apiKey(id = 1) == apiKey(id = 2)
            apiKey(name = "one") == apiKey(name = "two")
            apiKey(value = "one") == apiKey(value = "two")
            apiKey() == apiKey(environmentActivation = mutableMapOf())
        })
    }

    @Test
    fun hashCode_test() {
        Assertions.assertTrue({
            apiKey().hashCode() == apiKey().hashCode()
        })
    }

    private fun apiKey(
        id: Long = 1,
        name: String = "name",
        value: String = "value",
        environmentActivation: MutableMap<String, Boolean> = environmentActivation()
    ) = ApiKey(id, name, value, environmentActivation)

    private fun environmentActivation() = mutableMapOf(
        "dev" to true, "prod" to true
    )
}
