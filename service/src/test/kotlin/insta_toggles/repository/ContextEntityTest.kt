package insta_toggles.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ContextEntityTest {

    @Test
    fun id_notNull_test() {
        assertThrows<IllegalStateException> {
            ContextEntity(null, "key", "name", false).toDomain()
        }
    }

}
