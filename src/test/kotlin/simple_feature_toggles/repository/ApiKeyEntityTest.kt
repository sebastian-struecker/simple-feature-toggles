package simple_feature_toggles.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ApiKeyEntityTest {

    @Test
    fun constructor_noArgs_test() {
        val entity = ApiKeyEntity()
        assertNotNull(entity)
        assertNull(entity.id)
        assertEquals("", entity.name)
        assertNotNull(entity.value)
        assertTrue(entity.environmentActivation.isEmpty())
    }

    @Test
    fun constructor_allArgs_test() {
        val entity = apiKeyEntity()
        assertNotNull(entity)
        assertEquals(1L, entity.id)
        assertEquals("name", entity.name)
        assertNotNull(entity.value)
        assertTrue(entity.environmentActivation.isNotEmpty())
        assertEquals(2, entity.environmentActivation.size)
    }

    @Test
    fun constructor_nullId_test() {
        val entity = apiKeyEntity(id = null)
        assertNull(entity.id)
    }

    @Test
    fun toDomain_successful_test() {
        val domain = apiKeyEntity().toDomain()
        assertNotNull(domain)
        assertEquals(1L, domain.id)
        assertEquals("name", domain.name)
        assertEquals(2, domain.environmentActivation.size)
        assertEquals("dev", domain.environmentActivation.keys.first())
        assertTrue(domain.environmentActivation.values.first())
        assertEquals("prod", domain.environmentActivation.keys.elementAt(1))
        assertTrue(domain.environmentActivation.values.elementAt(1))
    }

    @Test
    fun toDomain_nullId_test() {
        assertThrows<IllegalStateException> {
            apiKeyEntity(null).toDomain()
        }
    }

    private fun apiKeyEntity(id: Long? = 1): ApiKeyEntity {
        return ApiKeyEntity(id, "name", "value", environmentActivation())
    }

    private fun environmentActivation(): MutableMap<String, Boolean> {
        return mutableMapOf(
            "dev" to true, "prod" to true
        )
    }

}
