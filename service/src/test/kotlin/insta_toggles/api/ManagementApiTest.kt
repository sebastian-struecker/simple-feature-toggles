package insta_toggles.api

import insta_toggles.Feature
import insta_toggles.FeaturePanacheRepository
import insta_toggles.FeatureService
import insta_toggles.api.models.CreateFeatureRequest
import insta_toggles.api.models.PartialFeatureUpdateRequest
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.quarkus.test.vertx.RunOnVertxContext
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


@QuarkusTest
@RunOnVertxContext
class ManagementApiTest {

    @Inject
    lateinit var serviceMock: FeatureService

    @Inject
    lateinit var repositoryMock: FeaturePanacheRepository

    @BeforeEach
    fun setupMocks() {
        repositoryMock = Mockito.mock(FeaturePanacheRepository::class.java)
        QuarkusMock.installMockForType(repositoryMock, FeaturePanacheRepository::class.java)

        serviceMock = Mockito.mock(FeatureService::class.java)
        QuarkusMock.installMockForType(serviceMock, FeatureService::class.java)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
        Mockito.reset(serviceMock)
    }

    companion object {
        const val baseUrl: String = "/management"
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
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getAll_authorized_viewer_test() {
        getAllRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun getAll_authorized_release_manager_test() {
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
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getByIdRequest().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun getById_authorized_viewer_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getByIdRequest().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun getById_authorized_release_manager_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getByIdRequest().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    fun create_unauthorized_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_authorized_admin_test() {
        val feature = feature()
        Mockito.`when`(serviceMock.create(feature.name, feature.description)).thenReturn(
            Uni.createFrom().item(feature)
        )
        val request = CreateFeatureRequest(feature.name, feature.description)
        createRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_unauthorized_viewer_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release-manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun create_unauthorized_release_manager_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    fun partialUpdate_unauthorized_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_notFound_test() {
        val feature = feature()
        val request = PartialFeatureUpdateRequest("updated", "updated", true)
        Mockito.`when`(
            serviceMock.update(
                feature.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(feature.apply {
                name = "updated"
                description = "updated"
                isActive = true
            })
        )
        partialUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_authorized_admin_test() {
        val feature = feature()
        val request = PartialFeatureUpdateRequest("updated", "updated", true)
        Mockito.`when`(
            serviceMock.update(
                feature.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(feature.apply {
                name = "updated"
                description = "updated"
                isActive = true
            })
        )
        partialUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialUpdate_unauthorized_viewer_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun partialUpdate_unauthorized_release_manager_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(403)
    }

    private fun getAllRequest(): Response = given().`when`().get(baseUrl)

    private fun getByIdRequest(id: Long = 1): Response = given().`when`().get("$baseUrl/$id")

    private fun createRequest(request: CreateFeatureRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(baseUrl)

    private fun partialUpdateRequest(request: PartialFeatureUpdateRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$baseUrl/$id")

    private fun feature() = Feature(1L, "name", "description", false)

}
