package simple_feature_toggles

import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ApiKey(
    val id: Long, var name: String, val value: String, val environmentActivation: MutableMap<String, Boolean>
) {
    init {
        checkInputs(name)
    }

    companion object {
        fun checkInputs(name: String) {
            require(name.isNotBlank()) {
                "Api key name must not be blank"
            }
        }

        fun generateValue(): String {
            val algorithm = "HmacSHA256"
            val mac = Mac.getInstance(algorithm)
            val secretKey = SecretKeySpec(generateRandomSecret().toByteArray(), algorithm)
            mac.init(secretKey)
            val rawHmac = mac.doFinal(generateRandomSecret().toByteArray())
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHmac).replace("-", "").replace("/", "")
                .replace("_", "").take(24)
        }

        private fun generateRandomSecret(): String {
            val date = Date.from(Instant.now())
            return "${UUID.randomUUID()}${UUID.randomUUID()}${UUID.randomUUID()}$date"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiKey

        if (id != other.id) return false
        if (name != other.name) return false
        if (value != other.value) return false
        if (environmentActivation != other.environmentActivation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + environmentActivation.hashCode()
        return result
    }
}
