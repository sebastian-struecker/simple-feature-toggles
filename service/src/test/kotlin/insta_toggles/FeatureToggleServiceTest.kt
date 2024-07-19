package insta_toggles


import insta_toggles.api.models.FeatureToggleFieldUpdateRequest
import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@QuarkusTest
@RunOnVertxContext
class FeatureToggleServiceTest {

    @Inject
    lateinit var featureToggleService: FeatureToggleService

    @Inject
    lateinit var repositoryMock: FeatureTogglePanacheRepository

    @BeforeEach
    fun setupMocks() {
        repositoryMock = Mockito.mock(FeatureTogglePanacheRepository::class.java)
        QuarkusMock.installMockForType(repositoryMock, FeatureTogglePanacheRepository::class.java)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
    }

    @Test
    fun create_test() {
        featureToggleService.create("key", "name", "description").subscribe()
            .withSubscriber(UniAssertSubscriber.create()).awaitItem()
        Mockito.verify(repositoryMock).create("key", "name", "description")
    }

    @Test
    fun update_name_test() {
        val featureToggle = FeatureToggle(
            1, "key", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
        )
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().item(featureToggle))
        featureToggleService.update(1, FeatureToggleFieldUpdateRequest("updated", null, null)).subscribe()
            .withSubscriber(UniAssertSubscriber.create()).awaitItem()
        featureToggle.apply {
            name = "updated"
        }
        Mockito.verify(repositoryMock).update(featureToggle)
    }

    @Test
    fun update_description_test() {
        val featureToggle = FeatureToggle(
            1, "key", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
        )
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().item(featureToggle))
        featureToggleService.update(1, FeatureToggleFieldUpdateRequest(null, "updated", null)).subscribe()
            .withSubscriber(UniAssertSubscriber.create()).awaitItem()
        featureToggle.apply {
            description = "updated"
        }
        Mockito.verify(repositoryMock).update(featureToggle)
    }

    @Test
    fun update_activation_test() {
        val featureToggle = FeatureToggle(
            1, "key", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
        )
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().item(featureToggle))
        featureToggleService.update(
            1, FeatureToggleFieldUpdateRequest(
                null, null, mutableMapOf(Context.TESTING to true)
            )
        ).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem()
        featureToggle.apply {
            activation.set(Context.TESTING, true)
        }
        Mockito.verify(repositoryMock).update(featureToggle)
    }

    @Test
    fun update_featureNotFound_test() {
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().failure(NoSuchElementException()))
        featureToggleService.update(
            1, FeatureToggleFieldUpdateRequest(null, null, null)
        ).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitFailure()
            .assertFailedWith(NoSuchElementException::class.java)
    }

}


