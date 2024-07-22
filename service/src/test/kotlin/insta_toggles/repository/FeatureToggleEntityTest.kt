package insta_toggles.repository

import insta_toggles.ContextName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FeatureToggleEntityTest {

    @Test
    fun constructor_noArgs_test() {
        val featureToggle = FeatureToggleEntity()
        assertNotNull(featureToggle)
        assertNull(featureToggle.id)
        assertEquals("", featureToggle.key)
        assertEquals("", featureToggle.name)
        assertEquals("", featureToggle.description)
        assertTrue(featureToggle.contexts.isNotEmpty())
        assertEquals(2, featureToggle.contexts.size)
    }

    @Test
    fun constructor_allArgs_test() {
        val featureToggle = featureToggleEntity()
        assertNotNull(featureToggle)
        assertEquals(1L, featureToggle.id)
        assertEquals("key", featureToggle.key)
        assertEquals("name", featureToggle.name)
        assertEquals("description", featureToggle.description)
        assertTrue(featureToggle.contexts.isNotEmpty())
        assertEquals(2, featureToggle.contexts.size)
    }

    @Test
    fun constructor_nullId_test() {
        val featureToggle = featureToggleEntity(null)
        assertNull(featureToggle.id)
    }

    @Test
    fun constructor_emptyContexts_test() {
        val featureToggle = FeatureToggleEntity(1L, "key", "name", "description", mutableListOf())
        assertEquals(2, featureToggle.contexts.size)
    }

    @Test
    fun defaultContexts_test() {
        val contexts = FeatureToggleEntity().contexts
        assertNotNull(contexts)
        assertEquals(2, contexts.size)
        assertEquals("testing", contexts[0].key)
        assertEquals("production", contexts[1].key)
    }

    @Test
    fun toDomain_successful_test() {
        val domain = featureToggleEntity().toDomain()
        assertNotNull(domain)
        assertEquals(1L, domain.id)
        assertEquals("key", domain.key)
        assertEquals("name", domain.name)
        assertEquals("description", domain.description)
        assertEquals(2, domain.contexts.size)
        assertEquals("testing", domain.contexts[0].key)
        assertEquals("testing", domain.contexts[0].name)
        assertFalse(domain.contexts[0].isActive)
        assertEquals("production", domain.contexts[1].key)
        assertEquals("production", domain.contexts[1].name)
        assertFalse(domain.contexts[1].isActive)
    }

    @Test
    fun toDomain_nullId_test() {
        assertThrows<IllegalStateException> {
            featureToggleEntity(null).toDomain()
        }
    }

    fun featureToggleEntity(id: Long? = 1): FeatureToggleEntity {
        return FeatureToggleEntity(id, "key", "name", "description", contextEntities())
    }

    private fun contextEntities(): MutableList<ContextEntity> {
        return mutableListOf(
            ContextEntity(
                1, ContextName.testing.toString(), ContextName.testing.toString(), false
            ), ContextEntity(
                2, ContextName.production.toString(), ContextName.production.toString(), false
            )
        )
    }

}
