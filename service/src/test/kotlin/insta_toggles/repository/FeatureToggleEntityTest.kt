package insta_toggles.repository

import insta_toggles.Context
import insta_toggles.ContextName
import insta_toggles.FeatureToggle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FeatureToggleEntityTest {

    @Test
    fun id_notNull_test() {
        assertThrows<IllegalStateException> {
            FeatureToggleEntity(
                null, "key", "name", "description", mutableListOf()
            ).toDomain()
        }
    }

    @Test
    fun defaultContexts_test() {
        val entity = FeatureToggleEntity(1, "key", "name", "description", mutableListOf())
        assert(entity.contexts.isNotEmpty())
    }

    @Test
    fun toDomain_test() {
        val entity = FeatureToggleEntity(1, "key", "name", "description", contextEntities())
        assert(featureToggle() == entity.toDomain())
    }

    private fun contextEntities(): MutableList<ContextEntity> {
        return mutableListOf(
            ContextEntity(1, ContextName.testing.toString(), ContextName.testing.toString()),
            ContextEntity(2, ContextName.production.toString(), ContextName.production.toString())
        )
    }

    private fun featureToggle(
        id: Long = 1, key: String = "key", name: String = "name", description: String = "description"
    ) = FeatureToggle(id, key, name, description, contexts())

    private fun contexts() = listOf(
        Context(1, ContextName.testing.toString(), ContextName.testing.toString(), false),
        Context(2, ContextName.production.toString(), ContextName.production.toString(), false)
    )
}
