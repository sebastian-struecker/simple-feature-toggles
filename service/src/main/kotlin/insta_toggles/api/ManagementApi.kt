package insta_toggles.api

import insta_toggles.Feature
import insta_toggles.FeatureRepository
import insta_toggles.FeatureService
import insta_toggles.api.models.CreateFeatureRequest
import insta_toggles.api.models.FeatureResponse
import insta_toggles.api.models.PartialFeatureUpdateRequest
import io.quarkus.logging.Log
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.NoCache
import org.jboss.resteasy.reactive.RestResponse

@ApplicationScoped
@Path("/management")
class ManagementApi(val featureService: FeatureService, val featureRepository: FeatureRepository) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    fun getAll(): Multi<FeatureResponse> {
        Log.debug("[ManagementApi] Calling method: get url: /management")
        return featureRepository.getAll().map { it.toResponse() }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    fun get(id: Long): Uni<RestResponse<FeatureResponse>> {
        Log.debug("[ManagementApi] Calling method: get url: /management/$id")
        return featureRepository.getById(id).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    fun create(request: CreateFeatureRequest): Uni<RestResponse<FeatureResponse>> {
        Log.debug("[ManagementApi] Calling method: post url: /management body: $request")
        return featureService.create(request.name, request.description).onItem()
            .transform { RestResponse.ok(it.toResponse()) }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed(DefaultRoles.ADMIN)
    fun partialUpdate(id: Long, updates: PartialFeatureUpdateRequest): Uni<RestResponse<FeatureResponse>> {
        Log.debug("[ManagementApi] Calling method: patch url: /management/$id body: $updates")
        return featureService.update(id, updates).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    fun Feature.toResponse(): FeatureResponse {
        return FeatureResponse(id, name, description, isActive)
    }
}
