package simple_feature_toggles.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class FeatureToggleEntityTest {

    @Test
    fun constructor_noArgs_test() {
        val entity = FeatureToggleEntity()
        assertNotNull(entity)
        assertNull(entity.id)
        assertEquals("", entity.key)
        assertEquals("", entity.name)
        assertEquals("", entity.description)
        assertTrue(entity.environmentActivation.isEmpty())
    }

    @Test
    fun constructor_allArgs_test() {
        val entity = featureToggleEntity()
        assertNotNull(entity)
        assertEquals(1L, entity.id)
        assertEquals("key", entity.key)
        assertEquals("name", entity.name)
        assertEquals("description", entity.description)
        assertTrue(entity.environmentActivation.isNotEmpty())
        assertEquals(2, entity.environmentActivation.size)
    }

    @Test
    fun constructor_nullId_test() {
        val entity = featureToggleEntity(null)
        assertNull(entity.id)
    }

    @Test
    fun toDomain_successful_test() {
        val domain = featureToggleEntity().toDomain()
        assertNotNull(domain)
        assertEquals(1L, domain.id)
        assertEquals("key", domain.key)
        assertEquals("name", domain.name)
        assertEquals("description", domain.description)
        assertEquals(2, domain.environmentActivation.size)
        assertEquals("dev", domain.environmentActivation.keys.first())
        assertTrue(domain.environmentActivation.values.first())
        assertEquals("prod", domain.environmentActivation.keys.elementAt(1))
        assertTrue(domain.environmentActivation.values.elementAt(1))
    }

    @Test
    fun toDomain_nullId_test() {
        assertThrows<IllegalStateException> {
            featureToggleEntity(null).toDomain()
        }
    }

    private fun featureToggleEntity(id: Long? = 1): FeatureToggleEntity {
        return FeatureToggleEntity(
            id,
            "key",
            "name",
            "description",
            environmentEntities(),
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    }

    private fun environmentEntities(): MutableMap<String, Boolean> {
        return mutableMapOf(
            "dev" to true, "prod" to true
        )
    }

}
