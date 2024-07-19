package insta_toggles


import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
@RunOnVertxContext
class FeatureTogglePanacheRepositoryTest {

    @Inject
    lateinit var featurePanacheRepository: FeatureTogglePanacheRepository

    fun getAll_test() {
    }

    fun getAllActive_test() {
    }

    @Test
    fun getById_found_test() {
        val feature = createFeature()
        featurePanacheRepository.getById(feature.id).subscribe().withSubscriber(UniAssertSubscriber.create())
            .awaitItem().assertSubscribed().assertItem(feature)
    }

    @Test
    fun getById_notFound_test() {
        featurePanacheRepository.getById(99).subscribe().withSubscriber(UniAssertSubscriber.create()).assertFailed()
    }

    @Test
    fun getByName_found_test() {
        val feature = createFeature()
        featurePanacheRepository.getByKey(feature.name).subscribe().withSubscriber(UniAssertSubscriber.create())
            .awaitItem().assertSubscribed().assertItem(feature)
    }

    @Test
    fun getByName_notFound_test() {
        featurePanacheRepository.getByKey("test").subscribe().withSubscriber(UniAssertSubscriber.create())
            .assertFailed()
    }

    @Test
    fun create_test() {
        val feature = createFeature()
        assertEquals(1L, feature.id)
        assertEquals("name", feature.name)
        assertEquals("description", feature.description)
        assertEquals(false, feature.activation.get(Context.TESTING))
        assertEquals(false, feature.activation.get(Context.PRODUCTION))
    }

    fun update_test() {
    }

    private fun createFeature(): FeatureToggle {
        return featurePanacheRepository.create("key", "name", "description").subscribe()
            .withSubscriber(UniAssertSubscriber.create()).awaitItem().item
    }

}


