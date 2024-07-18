package insta_toggles


import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext

@QuarkusTest
@RunOnVertxContext
class FeaturePanacheRepositoryTest {

    @InjectMock
    var repositoryMock: FeaturePanacheRepository? = null

    fun getAll_test() {
    }

    fun getAllActive_test() {
    }

    fun getById_test() {
    }

    fun getByName_test() {
    }

    fun create_test() {
    }

    fun update_test() {
    }

}


