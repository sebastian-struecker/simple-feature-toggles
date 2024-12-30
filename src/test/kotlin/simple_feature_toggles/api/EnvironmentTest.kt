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
import simple_feature_toggles.DefaultRoles
import simple_feature_toggles.Environment
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.UpdateEnvironmentRequest
import simple_feature_toggles.repository.EnvironmentPanacheRepository


@QuarkusTest
class EnvironmentTest {

    @InjectMock
    lateinit var repositoryMock: EnvironmentPanacheRepository

    @BeforeEach
    fun setupMocks() {
        Mockito.reset(repositoryMock)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
    }

    companion object {
        const val BASE_URL: String = "/environments"
    }

    @Test
    fun getAll_unauthorized_test() {
        getAllRequest().then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getAll_authorized_admin_test() {
        getAllRequest().then().statusCode(200)
    }

    @Test
    fun getById_unauthorized_test() {
        getByIdRequest(1).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getById_notFound_test() {
        getByIdRequest().then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getById_authorized_admin_test() {
        val environment = environment()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(environment)
        )
        getByIdRequest().then().statusCode(200).body("name", `is`(environment.name)).body("key", `is`(environment.key))
    }

    @Test
    fun create_unauthorized_test() {
        val request = CreateEnvironmentRequest("key", "name")
        createRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_authorized_admin_test() {
        val environment = environment()
        val request = CreateEnvironmentRequest("key", "name")
        Mockito.`when`(repositoryMock.create(request)).thenReturn(
            Uni.createFrom().item(environment)
        )
        createRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_unauthorized_viewer_test() {
        val request = CreateEnvironmentRequest("key", "name")
        createRequest(request).then().statusCode(403)
    }

    @Test
    fun partialUpdate_unauthorized_test() {
        val request = UpdateEnvironmentRequest(null)
        partialUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_notFound_test() {
        val request = UpdateEnvironmentRequest(null)
        partialUpdateRequest(request).then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_authorized_admin_test() {
        val apiKey = environment()
        val request = UpdateEnvironmentRequest("updated")
        Mockito.`when`(
            repositoryMock.update(
                apiKey.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(apiKey.apply {
                name = "updated"
            })
        )
        partialUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialUpdate_unauthorized_viewer_test() {
        val request = UpdateEnvironmentRequest(null)
        partialUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteById_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeById(1)
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteByIdRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteById_unauthorized_viewer_test() {
        deleteByIdRequest().then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteAll_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeAll()
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteAllRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteAll_unauthorized_viewer_test() {
        deleteAllRequest().then().statusCode(403)
    }

    private fun getAllRequest(): Response = given().`when`().get(BASE_URL)

    private fun getByIdRequest(id: Long = 1): Response = given().`when`().get("$BASE_URL/$id")

    private fun createRequest(request: CreateEnvironmentRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(BASE_URL)

    private fun partialUpdateRequest(request: UpdateEnvironmentRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$BASE_URL/$id")

    private fun deleteByIdRequest(id: Long = 1): Response = given().`when`().delete("$BASE_URL/$id")

    private fun deleteAllRequest(): Response = given().`when`().delete(BASE_URL)

    private fun environment() = Environment(1L, "key", "name")

}
