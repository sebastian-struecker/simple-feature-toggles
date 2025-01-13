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
class EnvironmentApiTest {

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
    fun getAll_Environments_unauthorized_test() {
        getAllEnvironmentsRequest().then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getAll_Environments_authorized_admin_test() {
        getAllEnvironmentsRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getAll_Environments_authorized_viewer_test() {
        getAllEnvironmentsRequest().then().statusCode(200)
    }

    @Test
    fun getEnvironmentById_unauthorized_test() {
        getEnvironmentByIdRequest(1).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getEnvironmentById_notFound_test() {
        getEnvironmentByIdRequest().then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getEnvironmentById_authorized_admin_test() {
        val environment = environment()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(environment)
        )
        getEnvironmentByIdRequest().then().statusCode(200).body("name", `is`(environment.name))
            .body("key", `is`(environment.key))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getEnvironmentById_authorized_viewer_test() {
        val environment = environment()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(environment)
        )
        getEnvironmentByIdRequest().then().statusCode(200).body("name", `is`(environment.name))
            .body("key", `is`(environment.key))
    }

    @Test
    fun create_Environment_unauthorized_test() {
        val request = CreateEnvironmentRequest("key", "name")
        createEnvironmentRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_Environment_authorized_admin_test() {
        val environment = environment()
        val request = CreateEnvironmentRequest("key", "name")
        Mockito.`when`(repositoryMock.create(request)).thenReturn(
            Uni.createFrom().item(environment)
        )
        createEnvironmentRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_Environment_unauthorized_viewer_test() {
        val request = CreateEnvironmentRequest("key", "name")
        createEnvironmentRequest(request).then().statusCode(403)
    }

    @Test
    fun partialEnvironmentUpdate_unauthorized_test() {
        val request = UpdateEnvironmentRequest(null)
        partialEnvironmentUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialEnvironmentUpdate_notFound_test() {
        val request = UpdateEnvironmentRequest(null)
        partialEnvironmentUpdateRequest(request).then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialEnvironmentUpdate_authorized_admin_test() {
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
        partialEnvironmentUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialEnvironmentUpdate_unauthorized_viewer_test() {
        val request = UpdateEnvironmentRequest(null)
        partialEnvironmentUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteEnvironmentById_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeById(1)
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteEnvironmentByIdRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteEnvironmentById_unauthorized_viewer_test() {
        deleteEnvironmentByIdRequest().then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteAll_Environments_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeAll()
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteAllEnvironmentsRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteAll_Environments_unauthorized_viewer_test() {
        deleteAllEnvironmentsRequest().then().statusCode(403)
    }

    private fun getAllEnvironmentsRequest(): Response = given().`when`().get(BASE_URL)

    private fun getEnvironmentByIdRequest(id: Long = 1): Response = given().`when`().get("$BASE_URL/$id")

    private fun createEnvironmentRequest(request: CreateEnvironmentRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(BASE_URL)

    private fun partialEnvironmentUpdateRequest(request: UpdateEnvironmentRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$BASE_URL/$id")

    private fun deleteEnvironmentByIdRequest(id: Long = 1): Response = given().`when`().delete("$BASE_URL/$id")

    private fun deleteAllEnvironmentsRequest(): Response = given().`when`().delete(BASE_URL)

    private fun environment() = Environment(1L, "key", "name")

}
