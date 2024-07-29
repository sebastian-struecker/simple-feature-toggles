package insta_toggles.api

import insta_toggles.Context
import insta_toggles.ContextName
import insta_toggles.FeatureToggle
import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.http.Headers
import io.restassured.response.Response
import io.smallrye.mutiny.Multi
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*


@QuarkusTest
class ClientApiTest {

    @InjectMock
    lateinit var repositoryMock: FeatureTogglePanacheRepository

    @BeforeEach
    fun setupMocks() {
        Mockito.reset(repositoryMock)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
    }

    companion object {
        const val BASE_URL: String = "/client"
        const val FEATURE_TOGGLES_URL: String = "feature-toggles"
    }

    @Test
    fun getAllActiveFeatures_context_invalid_test() {
        val feature = feature()
        Mockito.`when`(
            repositoryMock.getAllActive(ContextName.testing)
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
        getAllActiveFeaturesRequest(context = "dummy").then().statusCode(200).body("size()", `is`(0))
    }

    @Test
    fun getAllActiveFeatures_missingApiKey_test() {
        getAllActiveFeaturesRequest(apiKey = null).then().statusCode(401)
    }

    @Test
    fun getAllActiveFeatures_wrongApiKey_test() {
        getAllActiveFeaturesRequest(apiKey = "fail").then().statusCode(401)
    }

    @Test
    fun getAllActiveFeatures_apiKeyDisabled_test() {
        QuarkusMock.installMockForType(
            ApiKeyFilter(
                Optional.empty()
            ), ApiKeyFilter::class.java
        )
        getAllActiveFeaturesRequest(apiKey = "fail").then().statusCode(200)
    }

    @Test
    fun getAllActiveFeatures_testing_test() {
        val feature = feature()
        Mockito.`when`(
            repositoryMock.getAllActive(ContextName.testing)
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
        getAllActiveFeaturesRequest().then().statusCode(200).body("size()", `is`(1))
    }

    @Test
    fun getAllActiveFeatures_production_test() {
        val feature = feature()
        Mockito.`when`(
            repositoryMock.getAllActive(ContextName.production)
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
        getAllActiveFeaturesRequest(ContextName.production.toString()).then().statusCode(200).body("size()", `is`(1))
    }

    private fun getAllActiveFeaturesRequest(context: String = "testing", apiKey: String? = "test"): Response {
        if (apiKey == null) {
            return given().`when`().get("$BASE_URL/$FEATURE_TOGGLES_URL/$context")
        }
        val headers = Headers.headers(Header("x-api-key", apiKey))
        return given().`when`().headers(headers).get("$BASE_URL/$FEATURE_TOGGLES_URL/$context")
    }

    private fun feature() = FeatureToggle(
        1L, "key", "name", "description", listOf(
            Context(1, ContextName.testing.toString(), ContextName.testing.toString(), false),
            Context(2, ContextName.production.toString(), ContextName.production.toString(), false)
        )
    )

}
