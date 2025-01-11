package simple_feature_toggles.api

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.http.Headers
import io.restassured.response.Response
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import simple_feature_toggles.ApiKey
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.repository.ApiKeyPanacheRepository
import simple_feature_toggles.repository.FeatureTogglePanacheRepository


@QuarkusTest
class ClientApiTest {

    @InjectMock
    lateinit var featureToggleRepositoryMock: FeatureTogglePanacheRepository

    @InjectMock
    lateinit var apiKeyPanacheRepository: ApiKeyPanacheRepository

    @BeforeEach
    fun setupMocks() {
        Mockito.reset(featureToggleRepositoryMock)
        Mockito.reset(apiKeyPanacheRepository)
        QuarkusMock.installMockForType(
            ApiKeyFilter(
                apiKeyPanacheRepository
            ), ApiKeyFilter::class.java
        )
        val feature = feature()
        Mockito.`when`(
            featureToggleRepositoryMock.getAllActive("dev")
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(featureToggleRepositoryMock)
        Mockito.reset(apiKeyPanacheRepository)
    }

    companion object {
        const val BASE_URL: String = "/client"
        const val FEATURE_TOGGLES_URL: String = "feature-toggles"
    }

    @Test
    fun getAllActiveFeatures_environmentNotFound_test() {
        val apiKey = ApiKey(1, "dev", "test", mutableMapOf("dev" to true))
        Mockito.`when`(
            apiKeyPanacheRepository.getAll()
        ).thenReturn(
            Uni.createFrom().item(listOf(apiKey))
        )
        getAllActiveFeaturesRequest(environment = "dummy").then().statusCode(401)
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
    fun getAllActiveFeatures_dev_active_test() {
        val apiKey = ApiKey(1, "dev", "test", mutableMapOf("dev" to true))
        Mockito.`when`(
            apiKeyPanacheRepository.getAll()
        ).thenReturn(
            Uni.createFrom().item(listOf(apiKey))
        )
        getAllActiveFeaturesRequest().then().statusCode(200).body("size()", `is`(1))
    }

    @Test
    fun getAllActiveFeatures_dev_disabled_test() {
        val apiKey = ApiKey(1, "dev", "test", mutableMapOf("dev" to false))
        Mockito.`when`(
            apiKeyPanacheRepository.getAll()
        ).thenReturn(
            Uni.createFrom().item(listOf(apiKey))
        )
        getAllActiveFeaturesRequest().then().statusCode(401)
    }

    private fun getAllActiveFeaturesRequest(environment: String = "dev", apiKey: String? = "test"): Response {
        if (apiKey == null) {
            return given().`when`().get("$BASE_URL/$FEATURE_TOGGLES_URL?environment=$environment")
        }
        val headers = Headers.headers(Header("x-api-key", apiKey))
        return given().`when`().headers(headers).get("$BASE_URL/$FEATURE_TOGGLES_URL?environment=$environment")
    }

    private fun feature(isDevActive: Boolean = false) = FeatureToggle(
        1L, "key", "name", "description", mutableMapOf(
            "dev" to isDevActive, "prod" to true
        )
    )

}
