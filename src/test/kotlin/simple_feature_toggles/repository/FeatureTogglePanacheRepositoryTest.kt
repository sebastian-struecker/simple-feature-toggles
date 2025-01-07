package simple_feature_toggles.repository


import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.UniAsserter
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.FeatureToggleRepository
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.CreateFeatureToggleRequest
import simple_feature_toggles.api.models.EnvironmentActivationApiModel
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest


@QuarkusTest
class FeatureTogglePanacheRepositoryTest {

    @Inject
    lateinit var repository: FeatureToggleRepository

    @Inject
    lateinit var environmentRepository: EnvironmentRepository

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
            repository.create(createFeatureToggleRequest()).chain { it ->
                repository.getAll().collect().asList()
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAllActive_whenNoActiveEnvironments_shouldReturnEmptyList(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter, false)
        asserter.assertThat({
            repository.create(createFeatureToggleRequest(environmentActivation = environmentActivation)).chain { it ->
                repository.getAllActive("dev").collect().asList()
            }
        }, {
            assertTrue(it.size == 0)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAllActive_whenActiveContext_shouldReturnListWithOneEntity(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter)
        asserter.assertThat({
            repository.create(createFeatureToggleRequest(environmentActivation = environmentActivation)).chain { it ->
                repository.getAllActive("dev").collect().asList()
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getById_whenEntityExists_shouldReturnEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createFeatureToggleRequest()).chain { it ->
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
            repository.create(createFeatureToggleRequest()).chain { it ->
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
        val environmentActivation = createEnvironments(asserter)

        asserter.assertThat({
            repository.create(
                CreateFeatureToggleRequest(
                    "key", "name", "description", environmentActivation
                )
            )
        }, {
            assertFeatureToggle(it)
            assertEnvironmentActivation(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun create_whenInvalidInput_shouldThrowException(asserter: UniAsserter) {
        assertThrows(
            IllegalArgumentException::class.java, { repository.create(createFeatureToggleRequest(key = "123")) })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityExists_shouldUpdateEntity(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter, true)
        asserter.assertThat({
            repository.create(createFeatureToggleRequest()).chain { it ->
                repository.update(
                    it.id, UpdateFeatureToggleRequest(
                        "updated", "updated", environmentActivation
                    )
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            it.name == "updated" && it.description == "updated" && it.environmentActivation.entries.first().value
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyName_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createFeatureToggleRequest()).chain { it ->
                repository.update(it.id, UpdateFeatureToggleRequest(name = "partially updated"))
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            it.name == "partially updated" && it.environmentActivation.entries.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyDescription_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createFeatureToggleRequest()).chain { it ->
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
        val environmentActivation = createEnvironments(asserter, false)
        asserter.assertThat({
            repository.create(createFeatureToggleRequest(environmentActivation = environmentActivation)).chain { it ->
                repository.update(
                    it.id, UpdateFeatureToggleRequest(
                        environmentActivations = listOf(
                            EnvironmentActivationApiModel("dev", true), EnvironmentActivationApiModel("prod", true)
                        )
                    )
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, { it ->
            it.environmentActivation.entries.first().value
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenNoUpdates_shouldNotChangeEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createFeatureToggleRequest()).chain { it ->
                repository.update(
                    1, UpdateFeatureToggleRequest()
                )
            }.chain { it ->
                repository.getById(1)
            }
        }, { it ->
            it.name == "name" && it.description == "description" && it.environmentActivation.isEmpty()
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
            repository.create(createFeatureToggleRequest()).chain { it ->
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

    private fun assertFeatureToggle(it: FeatureToggle) {
        assertEquals("key", it.key)
        assertEquals("name", it.name)
        assertEquals("description", it.description)
    }

    private fun assertEnvironmentActivation(it: FeatureToggle) {
        assertEquals("dev", it.environmentActivation.entries.elementAt(0).key)
        assertEquals(true, it.environmentActivation.entries.elementAt(0).value)
        assertEquals("prod", it.environmentActivation.entries.elementAt(1).key)
        assertEquals(true, it.environmentActivation.entries.elementAt(1).value)
    }

    private fun createEnvironments(
        asserter: UniAsserter, isActive: Boolean = true
    ): List<EnvironmentActivationApiModel> {
        asserter.assertThat({
            environmentRepository.create(CreateEnvironmentRequest("dev", "dev"))
        }, {
            assertNotNull(it)
        })
        asserter.assertThat({
            environmentRepository.create(CreateEnvironmentRequest("prod", "prod"))
        }, {
            assertNotNull(it)
        })
        return listOf(
            EnvironmentActivationApiModel("dev", isActive), EnvironmentActivationApiModel("prod", isActive)
        )
    }

    private fun createFeatureToggleRequest(
        key: String = "key", environmentActivation: List<EnvironmentActivationApiModel> = listOf()
    ): CreateFeatureToggleRequest {
        return CreateFeatureToggleRequest(
            key, "name", "description", environmentActivation
        )
    }


}
