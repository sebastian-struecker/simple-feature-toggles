package simple_feature_toggles.repository


import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.UniAsserter
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import simple_feature_toggles.ApiKey
import simple_feature_toggles.ApiKeyRepository
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.api.models.CreateApiKeyRequest
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.UpdateApiKeyRequest


@QuarkusTest
class ApiKeyPanacheRepositoryTest {

    @Inject
    lateinit var repository: ApiKeyRepository

    @Inject
    lateinit var environmentRepository: EnvironmentRepository

    @Test
    @TestReactiveTransaction
    fun getAll_whenNoEntities_shouldReturnEmptyList(asserter: UniAsserter) {
        asserter.assertThat({
            repository.getAll()
        }, {
            it.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun getAll_whenOneEntity_shouldReturnListWithOneEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createApiKeyRequest()).chain { it ->
                repository.getAll()
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getById_whenEntityExists_shouldReturnEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createApiKeyRequest()).chain { it ->
                repository.getById(it.id)
            }
        }, {
            assertApiKey(it)
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
    fun create_whenValidInput_shouldCreateEntity(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter)

        asserter.assertThat({
            repository.create(
                createApiKeyRequest(
                    environmentActivation = environmentActivation
                )
            )
        }, {
            assertApiKey(it)
            assertEnvironmentActivation(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityExists_shouldUpdateEntity(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter, true)
        asserter.assertThat({
            repository.create(createApiKeyRequest()).chain { it ->
                repository.update(
                    it.id, UpdateApiKeyRequest(
                        "updated", environmentActivation
                    )
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            it.name == "updated" && it.environmentActivation.entries.first().value
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyName_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createApiKeyRequest()).chain { it ->
                repository.update(it.id, UpdateApiKeyRequest(name = "partially updated"))
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            it.name == "partially updated" && it.environmentActivation.entries.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenPartialUpdate_onlyEnvironment_shouldUpdateOnlyProvidedFields(asserter: UniAsserter) {
        val environmentActivation = createEnvironments(asserter, false)
        asserter.assertThat({
            repository.create(createApiKeyRequest(environmentActivation = environmentActivation)).chain { it ->
                repository.update(
                    it.id, UpdateApiKeyRequest(
                        environmentActivation = mutableMapOf(
                            "dev" to true, "prod" to true
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
            repository.create(createApiKeyRequest()).chain { it ->
                repository.update(
                    1, UpdateApiKeyRequest()
                )
            }.chain { it ->
                repository.getById(1)
            }
        }, { it ->
            it.name == "name" && it.environmentActivation.isEmpty()
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityDoesNotExist_shouldThrowException(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.update(1, UpdateApiKeyRequest("updated"))
        }, NoSuchElementException::class.java)
    }

    @Test
    @TestReactiveTransaction
    fun removeById_whenEntityExists_shouldRemoveEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createApiKeyRequest()).chain { it ->
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

    private fun assertApiKey(it: ApiKey) {
        assertEquals("name", it.name)
    }

    private fun assertEnvironmentActivation(it: ApiKey) {
        assertEquals("dev", it.environmentActivation.entries.elementAt(0).key)
        assertEquals(true, it.environmentActivation.entries.elementAt(0).value)
        assertEquals("prod", it.environmentActivation.entries.elementAt(1).key)
        assertEquals(true, it.environmentActivation.entries.elementAt(1).value)
    }

    private fun createEnvironments(
        asserter: UniAsserter, isActive: Boolean = true
    ): MutableMap<String, Boolean> {
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
        return mutableMapOf(
            "dev" to isActive, "prod" to isActive
        )
    }

    private fun createApiKeyRequest(
        name: String = "name", environmentActivation: MutableMap<String, Boolean> = mutableMapOf()
    ): CreateApiKeyRequest {
        return CreateApiKeyRequest(
            name, environmentActivation
        )
    }


}
