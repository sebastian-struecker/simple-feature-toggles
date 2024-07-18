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
    fun unauthorized_getAll_test() {
        getAllRequest().then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun authorized_admin_getAll_test() {
        getAllRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun authorized_viewer_getAll_test() {
        getAllRequest().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun authorized_release_manager_getAll_test() {
        getAllRequest().then().statusCode(200)
    }

    @Test
    fun unauthorized_getById_test() {
        getById(1).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun authorized_admin_getById_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getById().then().statusCode(200)
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun authorized_viewer_getById_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getById().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun authorized_release_manager_getById_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        getById().then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    @Test
    fun unauthorized_create_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun authorized_admin_create_test() {
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
    fun unauthorized_viewer_create_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release-manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun unauthorized_release_manager_create_test() {
        val request = CreateFeatureRequest("name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    fun unauthorized_partialUpdate_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun authorized_admin_partialUpdate_test() {
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
    fun unauthorized_viewer_partialUpdate_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun unauthorized_release_manager_partialUpdate_test() {
        val request = PartialFeatureUpdateRequest(null, null, isActive = true)
        partialUpdateRequest(request).then().statusCode(403)
    }

    private fun getAllRequest(): Response = given().`when`().get(baseUrl)

    private fun getById(id: Long = 1): Response = given().`when`().get("$baseUrl/$id")

    private fun createRequest(request: CreateFeatureRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(baseUrl)

    private fun partialUpdateRequest(request: PartialFeatureUpdateRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$baseUrl/$id")

    private fun feature() = Feature(1L, "name", "description", false)

}
