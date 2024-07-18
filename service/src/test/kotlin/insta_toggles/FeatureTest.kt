package insta_toggles

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FeatureTest {

    @Test
    fun validate_name_test() {
        assertThrows<IllegalArgumentException> { Feature(1, "", "description", false) }
    }
}
