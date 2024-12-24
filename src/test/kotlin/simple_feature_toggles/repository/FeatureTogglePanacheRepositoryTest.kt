package simple_feature_toggles.repository


import simple_feature_toggles.Context
import simple_feature_toggles.ContextName
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.api.models.ContextApiModel
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest
import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


@QuarkusTest
@RunOnVertxContext
class FeatureTogglePanacheRepositoryTest {

    @Inject
    lateinit var repository: FeatureTogglePanacheRepository

    @Test
    @TestReactiveTransaction
    fun getAll_whenNoEntities_shouldReturnEmptyList(asserter: UniAsserter) {
        asserter.assertThat({
            repository.getAll().collect().asList()
        }, {
            it.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAll_whenOneEntity_shouldReturnListWithOneEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.getAll().collect().asList()
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAllActive_whenNoActiveContexts_shouldReturnEmptyList(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.getAllActive(ContextName.testing).collect().asList()
            }
        }, {
            it.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAllActive_whenActiveProductionContext_shouldReturnListWithOneEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(
                    it.id, UpdateFeatureToggleRequest(
                        null, null, listOf(ContextApiModel(ContextName.production.toString(), true))
                    )
                ).chain { _ ->
                    repository.getAllActive(ContextName.production).collect().asList()
                }
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getById_whenEntityExists_shouldReturnEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.getById(it.id)
            }
        }, {
            assertFeatureToggle(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getById_whenEntityDoesNotExist_shouldThrowException(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.getById(1)
        }, NoSuchElementException::class.java)
    }

    @Test
    @TestReactiveTransaction
    fun getByKey_whenEntityExists_shouldReturnEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.getByKey(it.key)
            }
        }, {
            assertFeatureToggle(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getByKey_whenEntityDoesNotExist_shouldThrowException(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.getByKey("nonexistent")
        }, NoSuchElementException::class.java)
    }

    @Test
    @TestReactiveTransaction
    fun create_whenValidInput_shouldCreateEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description")
        }, {
            assertFeatureToggle(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun create_whenInvalidInput_shouldThrowException(asserter: UniAsserter) {
        Assertions.assertThrows(IllegalArgumentException::class.java,
            { repository.create("12_", "name", "description") })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityExists_shouldUpdateEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(
                    it.id, UpdateFeatureToggleRequest(
                        "updated", "updated", listOf(
                            ContextApiModel(ContextName.testing.toString(), true),
                            ContextApiModel(ContextName.production.toString(), true)
                        )
                    )
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            it.name == "updated" && it.description == "updated" && it.contexts[0].isActive
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyName_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(it.id, UpdateFeatureToggleRequest(name = "partially updated"))
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {

        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyDescription_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(it.id, UpdateFeatureToggleRequest(description = "partially updated"))
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            assertTrue(it.description == "partially updated")
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyContexts_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(
                    it.id,
                    UpdateFeatureToggleRequest(contexts = listOf(ContextApiModel(ContextName.testing.toString(), true)))
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, { it ->
            it.contexts.first { it.key == ContextName.production.toString() }.isActive
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenNoUpdates_shouldNotChangeEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.update(
                    1, UpdateFeatureToggleRequest()
                )
            }.chain { it ->
                repository.getById(1)
            }
        }, { it ->
            it.name == "name" && it.description == "description" && it.contexts.first { it.key == ContextName.production.toString() }.isActive
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityDoesNotExist_shouldThrowException(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.update(1, UpdateFeatureToggleRequest("updated", "updated"))
        }, NoSuchElementException::class.java)
    }

    @Test
    @TestReactiveTransaction
    fun removeById_whenEntityExists_shouldRemoveEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create("key", "name", "description").chain { it ->
                repository.removeById(it.id)
            }
        }, {
            assertTrue(it == Unit)
        })
    }

    @Test
    @TestReactiveTransaction
    fun removeAll_shouldRemoveAllEntities(asserter: UniAsserter) {
        asserter.assertThat({
            repository.removeAll()
        }, {
            assertTrue(it == Unit)
        })
    }

    @Test
    @TestReactiveTransaction
    fun createContexts_withTestContexts_returnTwoContexts(asserter: UniAsserter) {
        val contexts = repository.createContexts()
        assertEquals(2, contexts.size)
        assertContextEntity(contexts.get(0), ContextName.testing)
        assertContextEntity(contexts.get(1), ContextName.production)
    }

    private fun assertFeatureToggle(it: FeatureToggle) {
        assertEquals("key", it.key)
        assertEquals("name", it.name)
        assertEquals("description", it.description)
        assertContext(it.contexts[0], ContextName.testing)
    }

    private fun assertContext(context: Context, name: ContextName) {
        assertEquals(name.toString(), context.key)
        assertEquals(name.toString(), context.name)
        assertEquals(false, context.isActive)
    }

    private fun assertContextEntity(contextEntity: ContextEntity, name: ContextName) {
        assertEquals(name.toString(), contextEntity.key)
        assertEquals(name.toString(), contextEntity.name)
        assertEquals(false, contextEntity.isActive)
    }

}
