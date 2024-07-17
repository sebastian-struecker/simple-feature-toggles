package insta_toggles.api

import insta_toggles.Feature
import insta_toggles.FeatureRepository
import insta_toggles.api.models.FeatureToggleResponse
import io.smallrye.mutiny.Multi
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.NoCache

@ApplicationScoped
@Path("/features")
class FeatureApi(val featureRepository: FeatureRepository) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Multi<FeatureToggleResponse> {
        return featureRepository.getAllActive().onItem().transform { it.toFeatureToggleResponse() }
    }

    fun Feature.toFeatureToggleResponse(): FeatureToggleResponse {
        return FeatureToggleResponse(name)
    }

}
