package insta_toggles.api

import insta_toggles.Feature
import insta_toggles.FeaturePanacheRepository
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.restassured.RestAssured.given
import io.restassured.response.Response
import io.smallrye.mutiny.Multi
import jakarta.inject.Inject
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


@QuarkusTest
@RunOnVertxContext
class FeatureApiTest {

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

    companion object {
        const val baseUrl: String = "/features"
    }

    @Test
    fun getAllActive_test() {
        val feature = feature()
        Mockito.`when`(
            repositoryMock.getAllActive()
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
        val responseList =
            getAllActiveRequest().then().statusCode(200).body("size()", `is`(1)).extract().body().jsonPath()
                .getList<String>("")
        assert(responseList.get(0) == feature.name)
    }

    private fun getAllActiveRequest(): Response = given().`when`().get(baseUrl)

    private fun feature() = Feature(1L, "name", "description", false)

}
