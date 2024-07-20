package insta_toggles


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

    fun getAll_test() {
    }

    fun getAllActive_test() {
    }

    @Test
    fun getById_found_test(asserter: UniAsserter) {
        val featureToggle = featureToggle().toEntity()
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(featureToggle.id)).thenReturn(Uni.createFrom().item(featureToggle))
            asserter.assertEquals({ repositoryMock.findById(1) }, featureToggle)
        }
    }

    @Test
    fun getById_notFound_test(asserter: UniAsserter) {
        asserter.execute {
            Mockito.`when`(repositoryMock.findById(1)).thenReturn(Uni.createFrom().nullItem())
            asserter.assertFailedWith({ repositoryMock.findById(1) }, NoSuchElementException::class.java)
        }
    }

//    @Test
//    fun getByName_found_test() {
//        val feature = createFeature()
//        featurePanacheRepository.getByKey(feature.name).subscribe().withSubscriber(UniAssertSubscriber.create())
//            .awaitItem().assertSubscribed().assertItem(feature)
//    }
//
//    @Test
//    fun getByName_notFound_test() {
//        featurePanacheRepository.getByKey("test").subscribe().withSubscriber(UniAssertSubscriber.create())
//            .assertFailed()
//    }
//
//    @Test
//    fun create_test(asserter: UniAsserter) {
//        val feature = createFeature()
//        asserter.assertEquals(() -> feature, 1l)
//        assertEquals(1L, feature.id)
//        assertEquals("name", feature.name)
//        assertEquals("description", feature.description)
//        assertEquals(false, feature.activation.get(Context.TESTING))
//        assertEquals(false, feature.activation.get(Context.PRODUCTION))
//    }

    fun update_test() {
    }

    private fun featureToggle() = FeatureToggle(
        1L, "key", "name", "description", listOf(
            Context(1, ContextName.testing.toString(), ContextName.testing.toString(), false),
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
