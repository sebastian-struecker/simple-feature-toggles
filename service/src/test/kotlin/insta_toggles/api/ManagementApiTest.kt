package insta_toggles.api

import insta_toggles.Feature
import insta_toggles.FeaturePanacheRepository
import insta_toggles.FeatureService
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.quarkus.test.vertx.RunOnVertxContext
import io.restassured.RestAssured.given
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


@QuarkusTest
@RunOnVertxContext
class ManagementApiTest {

    @Inject lateinit var serviceMock: FeatureService

    @Inject lateinit var repositoryMock: FeaturePanacheRepository

    @BeforeEach
    fun setupMocks() {
        serviceMock = Mockito.mock(FeatureService::class.java)
        QuarkusMock.installMockForType(serviceMock, FeatureService::class.java)

        repositoryMock = Mockito.mock(FeaturePanacheRepository::class.java)
        QuarkusMock.installMockForType(repositoryMock, FeaturePanacheRepository::class.java)
    }

    companion object {
        const val managementUrl: String = "/management"
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun admin_getAll_test() {
        getAll_test()
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun viewer_getAll_test() {
        getAll_test()
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun release_manager_getAll_test() {
        getAll_test()
    }

    @Test
    @TestSecurity(user = "admin", roles = [DefaultRoles.ADMIN])
    fun admin_getById_test() {
        getById_test()
    }

    @Test
    @TestSecurity(user = "viewer", roles = [DefaultRoles.VIEWER])
    fun viewer_getById_test() {
        getById_test()
    }

    @Test
    @TestSecurity(user = "release_manager", roles = [DefaultRoles.RELEASE_MANAGER])
    fun release_manager_getById_test() {
        getById_test()
    }

    private fun getAll_test() {
        given().`when`().get(managementUrl).then().statusCode(200)
    }

    private fun getById_test() {
        val feature = feature()
        Mockito.`when`(repositoryMock.getById(1)).thenReturn(
            Uni.createFrom().item(feature)
        )
        given().`when`().get("$managementUrl/1").then().statusCode(200).body("name", `is`(feature.name))
            .body("description", `is`(feature.description))
    }

    private fun feature() = Feature(1L, "name", "description", false)

}
