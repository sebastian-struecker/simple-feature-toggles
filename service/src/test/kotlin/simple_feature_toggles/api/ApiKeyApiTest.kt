package simple_feature_toggles.api

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.smallrye.mutiny.Uni
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import simple_feature_toggles.ApiKey
import simple_feature_toggles.DefaultRoles
import simple_feature_toggles.api.models.CreateApiKeyRequest
import simple_feature_toggles.api.models.UpdateApiKeyRequest
import simple_feature_toggles.repository.ApiKeyPanacheRepository


@QuarkusTest
class ApiKeyApiTest {

    @InjectMock
    lateinit var repositoryMock: ApiKeyPanacheRepository

    @BeforeEach
    fun setupMocks() {
        Mockito.reset(repositoryMock)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
    }

    companion object {
        const val BASE_URL: String = "/api-keys"
    }

    @Test
    fun getAll_ApiKeys_unauthorized_test() {
        getAllApiKeysRequest().then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getAll_ApiKeys_authorized_admin_test() {
        getAllApiKeysRequest().then().statusCode(200)
    }

    @Test
    fun getApiKeyById_unauthorized_test() {
        getApiKeyByIdRequest(1).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getApiKeyById_notFound_test() {
        getApiKeyByIdRequest().then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getApiKeyById_authorized_admin_test() {
        val apiKey = apiKey()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(apiKey)
        )
        getApiKeyByIdRequest().then().statusCode(200).body("name", `is`(apiKey.name))
    }

    @Test
    fun create_ApiKey_unauthorized_test() {
        val request = CreateApiKeyRequest("name")
        createApiKeyRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_ApiKey_authorized_admin_test() {
        val apiKey = apiKey()
        val request = CreateApiKeyRequest("name")
        Mockito.`when`(repositoryMock.create(request)).thenReturn(
            Uni.createFrom().item(apiKey)
        )
        createApiKeyRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_ApiKey_unauthorized_viewer_test() {
        val request = CreateApiKeyRequest("name")
        createApiKeyRequest(request).then().statusCode(403)
    }

    @Test
    fun partialApiKeyUpdate_unauthorized_test() {
        val request = UpdateApiKeyRequest(null)
        partialApiKeyUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialApiKeyUpdate_notFound_test() {
        val request = UpdateApiKeyRequest(null)
        partialApiKeyUpdateRequest(request).then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialApiKeyUpdate_authorized_admin_test() {
        val apiKey = apiKey()
        val request = UpdateApiKeyRequest("updated")
        Mockito.`when`(
            repositoryMock.update(
                apiKey.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(apiKey.apply {
                name = "updated"
            })
        )
        partialApiKeyUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialApiKeyUpdate_unauthorized_viewer_test() {
        val request = UpdateApiKeyRequest(null)
        partialApiKeyUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteApiKeyById_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeById(1)
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteApiKeyByIdRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteApiKeyById_unauthorized_viewer_test() {
        deleteApiKeyByIdRequest().then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteAll_ApiKeys_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeAll()
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteAllApiKeysRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteAll_ApiKeys_unauthorized_viewer_test() {
        deleteAllApiKeysRequest().then().statusCode(403)
    }

    private fun getAllApiKeysRequest(): Response = given().`when`().get(BASE_URL)

    private fun getApiKeyByIdRequest(id: Long = 1): Response = given().`when`().get("$BASE_URL/$id")

    private fun createApiKeyRequest(request: CreateApiKeyRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(BASE_URL)

    private fun partialApiKeyUpdateRequest(request: UpdateApiKeyRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$BASE_URL/$id")

    private fun deleteApiKeyByIdRequest(id: Long = 1): Response = given().`when`().delete("$BASE_URL/$id")

    private fun deleteAllApiKeysRequest(): Response = given().`when`().delete(BASE_URL)

    private fun apiKey() = ApiKey(
        1L, "name", "value", mutableMapOf(
            "dev" to true,
            "prod" to true,
        )
    )

}
