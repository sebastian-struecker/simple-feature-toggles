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
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.api.models.CreateFeatureToggleRequest
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest
import simple_feature_toggles.repository.FeatureTogglePanacheRepository


@QuarkusTest
class FeatureToggleApiTest {

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
        const val BASE_URL: String = "/feature-toggles"
    }

    @Test
    fun getAll_FeatureToggles_unauthorized_test() {
        getAllFeatureTogglesRequest().then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getAll_FeatureToggles_authorized_admin_test() {
        getAllFeatureTogglesRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getAll_FeatureToggles_authorized_viewer_test() {
        getAllFeatureTogglesRequest().then().statusCode(200)
    }

    @Test
    fun getFeatureToggleById_unauthorized_test() {
        getFeatureToggleByIdRequest(1).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getFeatureToggleById_notFound_test() {
        getFeatureToggleByIdRequest().then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun getFeatureToggleById_authorized_admin_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getFeatureToggleByIdRequest().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getFeatureToggleById_authorized_viewer_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getFeatureToggleByIdRequest().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    fun create_FeatureToggle_unauthorized_test() {
        val request = CreateFeatureToggleRequest("key", "name", "description")
        createFeatureToggleRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_FeatureToggle_authorized_admin_test() {
        val feature = feature()
        val request = CreateFeatureToggleRequest(feature.key, feature.name, feature.description, mutableMapOf())
        Mockito.`when`(repositoryMock.create(request)).thenReturn(
            Uni.createFrom().item(feature)
        )
        createFeatureToggleRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_FeatureToggle_unauthorized_viewer_test() {
        val request = CreateFeatureToggleRequest("key", "name", "description")
        createFeatureToggleRequest(request).then().statusCode(403)
    }

    @Test
    fun partialFeatureToggleUpdate_unauthorized_test() {
        val request = UpdateFeatureToggleRequest(null, null, null)
        partialFeatureToggleUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialFeatureToggleUpdate_notFound_test() {
        val request = UpdateFeatureToggleRequest(null, null, null)
        partialFeatureToggleUpdateRequest(request).then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialFeatureToggleUpdate_authorized_admin_test() {
        val feature = feature()
        val request = UpdateFeatureToggleRequest(
            "updated", "updated", null
        )
        Mockito.`when`(
            repositoryMock.update(
                feature.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(feature.apply {
                name = "updated"
                description = "updated"
            })
        )
        partialFeatureToggleUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialFeatureToggleUpdate_unauthorized_viewer_test() {
        val request = UpdateFeatureToggleRequest(null, null, null)
        partialFeatureToggleUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteFeatureToggleById_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeById(1)
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteFeatureToggleByIdRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteFeatureToggleById_unauthorized_viewer_test() {
        deleteFeatureToggleByIdRequest().then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun deleteAll_FeatureToggles_authorized_admin_test() {
        Mockito.`when`(
            repositoryMock.removeAll()
        ).thenReturn(
            Uni.createFrom().nullItem()
        )
        deleteAllFeatureTogglesRequest().then().statusCode(204)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun deleteAll_FeatureToggles_unauthorized_viewer_test() {
        deleteAllFeatureTogglesRequest().then().statusCode(403)
    }

    private fun getAllFeatureTogglesRequest(): Response = given().`when`().get(BASE_URL)

    private fun getFeatureToggleByIdRequest(id: Long = 1): Response = given().`when`().get("$BASE_URL/$id")

    private fun createFeatureToggleRequest(request: CreateFeatureToggleRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(BASE_URL)

    private fun partialFeatureToggleUpdateRequest(request: UpdateFeatureToggleRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$BASE_URL/$id")

    private fun deleteFeatureToggleByIdRequest(id: Long = 1): Response = given().`when`().delete("$BASE_URL/$id")

    private fun deleteAllFeatureTogglesRequest(): Response = given().`when`().delete(BASE_URL)

    private fun feature() = FeatureToggle(
        1L, "key", "name", "description", mutableMapOf(
            "dev" to true,
            "prod" to true,
        )
    )

}
