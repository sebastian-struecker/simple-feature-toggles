package simple_feature_toggles.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EnvironmentEntityTest {

    @Test
    fun constructor_noArgs_test() {
        val context = EnvironmentEntity()
        assertNotNull(context)
        assertNull(context.id)
        assertEquals("", context.key)
        assertEquals("", context.name)
    }

    @Test
    fun constructor_allArgs_test() {
        val context = EnvironmentEntity(1, "key", "name")
        assertNotNull(context)
        assertEquals(1L, context.id)
        assertEquals("key", context.key)
        assertEquals("name", context.name)
    }

    @Test
    fun constructor_nullId_test() {
        val context = EnvironmentEntity(null, "key", "name")
        assertNull(context.id)
    }

    @Test
    fun toDomain_idNull_test() {
        assertThrows<IllegalStateException> {
            EnvironmentEntity(null, "key", "name").toDomain()
        }
    }

}
