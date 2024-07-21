package insta_toggles


import insta_toggles.api.models.FeatureToggleUpdateRequest
import insta_toggles.repository.ContextEntity
import insta_toggles.repository.FeatureToggleEntity
import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import io.smallrye.mutiny.Uni
import org.junit.jupiter.api.Test
import org.mockito.Mockito


@QuarkusTest
@RunOnVertxContext
class FeatureTogglePanacheRepositoryTest {

    @InjectMock
    lateinit var repositoryMock: FeatureTogglePanacheRepository

    @Test
    fun getAll_test(asserter: UniAsserter) {
        val featureToggle = featureToggle().toEntity()
        asserter.execute {
            Mockito.`when`(repositoryMock.listAll()).thenReturn(Uni.createFrom().item(listOf(featureToggle)))
            asserter.assertEquals({ repositoryMock.listAll() }, listOf(featureToggle))
        }
    }

//    @Test
//    fun getAllActive_test(asserter: UniAsserter) {
//        val featureToggle = featureToggle().toEntity()
//        asserter.execute {
//            Mockito.`when`(repositoryMock.findById(featureToggle.id)).thenReturn(Uni.createFrom().item(featureToggle))
//            asserter.assertEquals({ repositoryMock.findById(1) }, featureToggle)
//        }
//    }

    @Test
    fun getById_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        val entity = featureToggle.toEntity()
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(featureToggle.id)).thenReturn(Uni.createFrom().item(entity))
            asserter.assertEquals({ repositoryMock.getById(1) }, featureToggle)
        }
    }

    @Test
    fun getById_notFound_test(asserter: UniAsserter) {
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(1)).thenReturn(Uni.createFrom().nullItem())
            asserter.assertFailedWith({ repositoryMock.getById(1) }, NoSuchElementException::class.java)
        }
    }

    @Test
    fun getByKey_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        val entity = featureToggle.toEntity()
        asserter.execute {
            Mockito.`when`(repositoryMock.findByKey(featureToggle.key)).thenReturn(
                Uni.createFrom().item(entity)
            )
            asserter.assertEquals({
                repositoryMock.getByKey(featureToggle.key)
            }, featureToggle)
        }
    }

    @Test
    fun getByKey_notFound_test(asserter: UniAsserter) {
        asserter.execute {
            Mockito.`when`(repositoryMock.findByKey("empty"))
                .thenReturn(Uni.createFrom().failure(NoSuchElementException()))
            asserter.assertFailedWith(
                { repositoryMock.getByKey("empty") }, NoSuchElementException::class.java
            )
        }
    }

    @Test
    fun create_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        val entity = featureToggle.toEntity()
        asserter.execute {
            Mockito.`when`(repositoryMock.persistAndFlush(entity)).thenReturn(Uni.createFrom().item(entity))
            asserter.assertEquals({
                repositoryMock.create(featureToggle.key, featureToggle.name, featureToggle.description)
            }, featureToggle)
        }
    }

    @Test
    fun create_wrongInput_test(asserter: UniAsserter) {
        asserter.execute {
            asserter.assertFailedWith(
                { repositoryMock.create("12_", "name", "description") }, IllegalArgumentException::class.java
            )
        }
    }

    @Test
    fun update_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        val entity = featureToggle.toEntity()
        val updateRequest = FeatureToggleUpdateRequest("updated", null, null)
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(featureToggle.id)).thenReturn(Uni.createFrom().item(entity))
            asserter.assertEquals({
                repositoryMock.update(featureToggle.id, updateRequest)
            }, featureToggle.apply {
                name = updateRequest.name!!
            })
        }
    }

    @Test
    fun update_notFound_test(asserter: UniAsserter) {
        val updateRequest = FeatureToggleUpdateRequest("updated", null, null)
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(1)).thenReturn(Uni.createFrom().nullItem())
            asserter.assertFailedWith(
                { repositoryMock.update(1, updateRequest) }, NoSuchElementException::class.java
            )
        }
    }

    @Test
    fun removeById_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle()
        asserter.execute {
            Mockito.`when`(repositoryMock.deleteById(featureToggle.id)).thenReturn(Uni.createFrom().item(true))
            asserter.assertEquals({
                repositoryMock.removeById(featureToggle.id)
            }, null)
        }
    }

    @Test
    fun removeById_notFound_test(asserter: UniAsserter) {
        asserter.execute {
            Mockito.`when`(repositoryMock.deleteById(1)).thenReturn(Uni.createFrom().nullItem())
            asserter.assertFailedWith(
                { repositoryMock.removeById(1) }, NoSuchElementException::class.java
            )
        }
    }

    @Test
    fun removeAll_found_test(asserter: UniAsserter) {
        asserter.execute {
            repositoryMock.removeAll()
            repositoryMock.deleteAll()
        }
    }

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
