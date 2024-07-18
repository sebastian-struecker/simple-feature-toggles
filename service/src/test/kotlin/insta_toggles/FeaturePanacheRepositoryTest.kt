package insta_toggles


import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import jakarta.inject.Inject

@QuarkusTest
@RunOnVertxContext
class FeaturePanacheRepositoryTest {

    @Inject
    lateinit var featurePanacheRepository: FeaturePanacheRepository

    fun getAll_test() {
    }

    fun getAllActive_test() {
    }

//    @Test
//    fun getById_test() {
//        featurePanacheRepository.getById(1).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem()
//            .assertSubscribed()
//    }

    fun getByName_test() {
    }

    fun create_test() {
    }

    fun update_test() {
    }

}


