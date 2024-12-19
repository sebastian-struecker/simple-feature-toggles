package simple_feature_toggles.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ContextEntityTest {

    @Test
    fun constructor_noArgs_test() {
        val context = ContextEntity()
        assertNotNull(context)
        assertNull(context.id)
        assertEquals("", context.key)
        assertEquals("", context.name)
        assertFalse(context.isActive)
    }

    @Test
    fun constructor_allArgs_test() {
        val context = ContextEntity(1, "key", "name", true)
        assertNotNull(context)
        assertEquals(1L, context.id)
        assertEquals("key", context.key)
        assertEquals("name", context.name)
        assertTrue(context.isActive)
    }

    @Test
    fun constructor_nullId_test() {
        val context = ContextEntity(null, "key", "name", false)
        assertNull(context.id)
    }

    @Test
    fun toDomain_idNull_test() {
        assertThrows<IllegalStateException> {
            ContextEntity(null, "key", "name", false).toDomain()
        }
    }

}
