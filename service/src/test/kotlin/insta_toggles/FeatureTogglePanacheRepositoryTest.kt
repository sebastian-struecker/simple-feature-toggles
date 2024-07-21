package insta_toggles


import insta_toggles.api.models.ContextApiModel
import insta_toggles.api.models.FeatureToggleUpdateRequest
import insta_toggles.repository.ContextEntity
import insta_toggles.repository.FeatureToggleEntity
import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


@QuarkusTest
@RunOnVertxContext
class FeatureTogglePanacheRepositoryTest {

    @Inject
    lateinit var repository: FeatureTogglePanacheRepository

    @Test
    fun getAll_size_0_test(asserter: UniAsserter) {
        asserter.assertThat({
            repository.getAll().collect().asList()
        }, {
            it.size == 0
        })
    }

    @Test
    fun getAll_size_1_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
        }
        asserter.assertThat({
            repository.getAll().collect().asList()
        }, {
            it.size == 1
        })
    }

    @Test
    fun getAllActive_noActive_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
        }
        asserter.assertThat({
            repository.getAllActive(ContextName.testing).collect().asList()
        }, {
            it.size == 0
        })
    }

    @Test
    fun getAllActive_activeTesting_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
            repository.update(
                1, FeatureToggleUpdateRequest(null, null, listOf(ContextApiModel(ContextName.testing.toString(), true)))
            )
        }
        asserter.assertThat({
            repository.getAllActive(ContextName.testing).collect().asList()
        }, {
            it.size == 1
        })
    }

    @Test
    fun getAllActive_activeProduction_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
            repository.update(
                1,
                FeatureToggleUpdateRequest(null, null, listOf(ContextApiModel(ContextName.production.toString(), true)))
            )
        }
        asserter.assertThat({
            repository.getAllActive(ContextName.production).collect().asList()
        }, {
            it.size == 1
        })
    }

//    TODO: fix me
//    @Test
//    fun getById_found_test(asserter: UniAsserter) {
//        val featureToggle = featureToggle()
//        asserter.execute {
//            repository.create("key", "name", "description")
//        }.assertThat({
//            repository.getById(1)
//        }, {
//            it == featureToggle
//        })
//    }

//    TODO: fix me
//    @Test
//    fun getById_notFound_test(asserter: UniAsserter) {
//        asserter.execute {
//            repository.getByKey("key")
//        }.assertFailedWith({
//            repository.getByKey("key")
//        }, NoSuchElementException::class.java)
//    }

    @Test
    fun getByKey_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        asserter.execute {
            repository.create("key", "name", "description")
        }
        asserter.assertThat({
            repository.getByKey("key")
        }, {
            it == featureToggle
        })
    }

    @Test
    fun getByKey_notFound_test(asserter: UniAsserter) {
        asserter.assertFailedWith({
            repository.getByKey("empty")
        }, NoSuchElementException::class.java)
    }

    @Test
    fun create_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        asserter.assertThat({
            repository.create("key", "name", "description")
        }, {
            it == featureToggle
        })
    }

    @Test
    fun create_wrongInput_test(asserter: UniAsserter) {
        Assertions.assertThrows(IllegalArgumentException::class.java,
            { repository.create("12_", "name", "description") })
    }

    @Test
    fun update_found_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
            repository.update(
                1, FeatureToggleUpdateRequest("updated", null, null)
            )
        }
        asserter.assertThat({
            repository.getById(1)
        }, {
            it.name == "updated"
        })
    }

//    TODO: fix me
//    @Test
//    fun update_notFound_test(asserter: UniAsserter) {
//        asserter.assertFailedWith({
//            repository.getByKey("key")
//        }, NoSuchElementException::class.java)
//    }

    @Test
    fun removeById_found_test(asserter: UniAsserter) {
        asserter.execute {
            repository.create("key", "name", "description")
        }
        asserter.assertSame({
            repository.removeById(1)
        }, Unit)
    }

    //
//    @Test
//    fun removeById_notFound_test(asserter: UniAsserter) {
//        asserter.execute {
//            Mockito.`when`(repositoryMock.deleteById(1)).thenReturn(Uni.createFrom().nullItem())
//            asserter.assertFailedWith(
//                { repositoryMock.removeById(1) }, NoSuchElementException::class.java
//            )
//        }
//    }
//
//    @Test//    fun removeAll_found_test(asserter: UniAsserter) {
//        asserter.execute {
//            repositoryMock.removeAll()
//            repositoryMock.deleteAll()
//        }
//    }

    private fun featureToggle(key: String = "key") = FeatureToggle(
        1L, key, "name", "description", listOf(
            Context(1, ContextName.testing.toString(), ContextName.testing.toString(), true),
            Context(2, ContextName.production.toString(), ContextName.production.toString(), false)
        )
    )

    private fun FeatureToggle.toEntity(): FeatureToggleEntity {
        return FeatureToggleEntity(id, key, name, description, contexts.map { it.toEntity() }.toMutableList())
    }

    private fun Context.toEntity(): ContextEntity {
        return ContextEntity(id, key, name, isActive)
    }

}
