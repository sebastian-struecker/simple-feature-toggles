package insta_toggles


import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext

@QuarkusTest
@RunOnVertxContext
class FeatureServiceTest {

    @InjectMock
    var repositoryMock: FeaturePanacheRepository? = null

    fun create_test() {
    }

    fun update_test() {
    }
}


