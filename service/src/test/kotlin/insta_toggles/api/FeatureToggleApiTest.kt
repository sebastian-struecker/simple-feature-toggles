package insta_toggles.api

import insta_toggles.Context
import insta_toggles.FeatureToggle
import insta_toggles.FeatureToggleService
import insta_toggles.api.models.CreateFeatureToggleRequest
import insta_toggles.api.models.FeatureToggleFieldUpdateRequest
import insta_toggles.repository.FeatureTogglePanacheRepository
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.quarkus.test.vertx.RunOnVertxContext
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


@QuarkusTest
@RunOnVertxContext
class FeatureToggleApiTest {

    @Inject
    lateinit var serviceMock: FeatureToggleService

    @Inject
    lateinit var repositoryMock: FeatureTogglePanacheRepository

    @BeforeEach
    fun setupMocks() {
        repositoryMock = Mockito.mock(FeatureTogglePanacheRepository::class.java)
        QuarkusMock.installMockForType(repositoryMock, FeatureTogglePanacheRepository::class.java)

        serviceMock = Mockito.mock(FeatureToggleService::class.java)
        QuarkusMock.installMockForType(serviceMock, FeatureToggleService::class.java)
    }

    @AfterEach
    fun resetMocks() {
        Mockito.reset(repositoryMock)
        Mockito.reset(serviceMock)
    }

    companion object {
        const val featureToggleUrl: String = "/feature-toggle"
        const val featureTogglesUrl: String = "/feature-toggles"
    }

    @Test
    fun getAllActiveFeatures_context_invalid_test() {
        val feature = feature()
        Mockito.`when`(
            repositoryMock.getAllActive(Context.TESTING)
        ).thenReturn(
            Multi.createFrom().item(feature)
        )
        val responseList =
            getAllActiveFeaturesRequest("asd").then().statusCode(200).body("size()", `is`(1)).extract().body().jsonPath()
                .getList<String>("")
        assert(responseList.get(0) == feature.name)
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
        val request = CreateFeatureToggleRequest("key", "name", "description")
        createRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun create_authorized_admin_test() {
        val feature = feature()
        Mockito.`when`(serviceMock.create(feature.key, feature.name, feature.description)).thenReturn(
            Uni.createFrom().item(feature)
        )
        val request = CreateFeatureToggleRequest(feature.key, feature.name, feature.description)
        createRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun create_unauthorized_viewer_test() {
        val request = CreateFeatureToggleRequest("key", "name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release-manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun create_unauthorized_release_manager_test() {
        val request = CreateFeatureToggleRequest("key", "name", "description")
        createRequest(request).then().statusCode(403)
    }

    @Test
    fun partialUpdate_unauthorized_test() {
        val request = FeatureToggleFieldUpdateRequest(null, null, null)
        partialUpdateRequest(request).then().statusCode(401)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_notFound_test() {
        val request = FeatureToggleFieldUpdateRequest(null, null, null)
        partialUpdateRequest(request).then().statusCode(404)
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun partialUpdate_authorized_admin_test() {
        val feature = feature()
        val request = FeatureToggleFieldUpdateRequest("updated", "updated", mapOf(Context.TESTING to true))
        Mockito.`when`(
            serviceMock.update(
                feature.id, request
            )
        ).thenReturn(
            Uni.createFrom().item(feature.apply {
                name = "updated"
                description = "updated"
                activation.set(Context.TESTING, true)
            })
        )
        partialUpdateRequest(request).then().statusCode(200).body("name", `is`(request.name))
            .body("description", `is`(request.description))
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun partialUpdate_unauthorized_viewer_test() {
        val request = FeatureToggleFieldUpdateRequest(null, null, null)
        partialUpdateRequest(request).then().statusCode(403)
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun partialUpdate_unauthorized_release_manager_test() {
        val request = FeatureToggleFieldUpdateRequest(null, null, null)
        partialUpdateRequest(request).then().statusCode(403)
    }

    private fun getAllActiveFeaturesRequest(context: String): Response =
        given().`when`().get("$featureTogglesUrl/$context/active")

    private fun getAllRequest(): Response = given().`when`().get(featureTogglesUrl)

    private fun getByIdRequest(id: Long = 1): Response = given().`when`().get("$featureToggleUrl/$id")

    private fun createRequest(request: CreateFeatureToggleRequest): Response =
        given().`when`().body(request).contentType(ContentType.JSON).post(featureTogglesUrl)

    private fun partialUpdateRequest(request: FeatureToggleFieldUpdateRequest, id: Long = 1): Response =
        given().`when`().body(request).contentType(ContentType.JSON).patch("$featureToggleUrl/$id")

    private fun feature() = FeatureToggle(
        1L, "key", "name", "description", mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)
    )

}
