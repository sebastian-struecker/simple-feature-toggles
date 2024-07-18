package insta_toggles


import insta_toggles.api.models.PartialFeatureUpdateRequest
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
class FeatureServiceTest {

    @Inject
    lateinit var featureService: FeatureService

    @Inject
    lateinit var repositoryMock: FeaturePanacheRepository

    @BeforeEach
    fun setupMocks() {
        repositoryMock = Mockito.mock(FeaturePanacheRepository::class.java)
        QuarkusMock.installMockForType(repositoryMock, FeaturePanacheRepository::class.java)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
    }

    @Test
    fun create_test() {
        featureService.create("name", "description").subscribe().withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
        Mockito.verify(repositoryMock).create("name", "description")
    }

    @Test
    fun update_featureFound_test() {
        val feature = Feature(1, "name", "description", false)
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().item(feature))
        featureService.update(1, PartialFeatureUpdateRequest("test", "test", true)).subscribe()
            .withSubscriber(UniAssertSubscriber.create()).awaitItem()
        feature.apply {
            name = "test"
            description = "test"
            isActive = true
        }
        Mockito.verify(repositoryMock).update(feature)
    }

    @Test
    fun update_featureNotFound_test() {
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(Uni.createFrom().failure(NoSuchElementException()))
        featureService.update(
            1, PartialFeatureUpdateRequest("test", "test", true)
        ).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitFailure()
            .assertFailedWith(NoSuchElementException::class.java)
    }

}


