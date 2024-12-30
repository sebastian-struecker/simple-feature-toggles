package simple_feature_toggles.repository


import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.UniAsserter
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import simple_feature_toggles.Environment
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.UpdateEnvironmentRequest


@QuarkusTest
class EnvironmentPanacheRepositoryTest {

    @Inject
    lateinit var repository: EnvironmentRepository

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
            repository.create(createEnvironmentRequest()).chain { it ->
                repository.getAll().collect().asList()
            }
        }, {
            assertTrue(it.size == 1)
        })
    }

    @Test
    @TestReactiveTransaction
    fun getById_whenEntityExists_shouldReturnEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createEnvironmentRequest()).chain { it ->
                repository.getById(it.id)
            }
        }, {
            assertEnvironment(it)
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
            repository.create(createEnvironmentRequest()).chain { it ->
                repository.getByKey(it.key)
            }
        }, {
            assertEnvironment(it)
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
            repository.create(createEnvironmentRequest())
        }, {
            assertEnvironment(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun create_whenInvalidInput_shouldThrowException(asserter: UniAsserter) {
        assertThrows(
            IllegalArgumentException::class.java, { repository.create(createEnvironmentRequest(key = "123")) })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityExists_shouldUpdateEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createEnvironmentRequest()).chain { it ->
                repository.update(
                    it.id, UpdateEnvironmentRequest("updated")
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, {
            assertSame("updated", it.name)
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenNoUpdates_shouldNotChangeEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createEnvironmentRequest()).chain { it ->
                repository.update(
                    it.id, UpdateEnvironmentRequest()
                )
            }.chain { it ->
                repository.getById(it.id)
            }
        }, { it ->
            assertEnvironment(it)
        })
    }

    @Test
    @TestReactiveTransaction
    fun update_whenEntityDoesNotExist_shouldThrowException(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.update(1, UpdateEnvironmentRequest("updated"))
        }, NoSuchElementException::class.java)
    }

    @Test
    @TestReactiveTransaction
    fun removeById_whenEntityExists_shouldRemoveEntity(asserter: UniAsserter) {
        asserter.assertThat({
            repository.create(createEnvironmentRequest()).chain { it ->
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

    private fun assertEnvironment(it: Environment) {
        assertEquals("key", it.key)
        assertEquals("name", it.name)
    }

    private fun createEnvironmentRequest(
        key: String = "key", name: String = "name"
    ): CreateEnvironmentRequest {
        return CreateEnvironmentRequest(key, name)
    }

}
