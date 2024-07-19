package insta_toggles.api

import insta_toggles.Context
import insta_toggles.FeatureToggle
import insta_toggles.FeatureToggleRepository
import insta_toggles.FeatureToggleService
import insta_toggles.api.models.CreateFeatureToggleRequest
import insta_toggles.api.models.FeatureToggleFieldUpdateRequest
import insta_toggles.api.models.FeatureToggleResponse
import io.quarkus.logging.Log
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.NoCache
import org.jboss.resteasy.reactive.RestResponse

@ApplicationScoped
@Path("")
class FeatureToggleApi(
    val featureToggleService: FeatureToggleService, val featureToggleRepository: FeatureToggleRepository
) {

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/feature-toggles/{context}/active")
    fun getAllActiveFeatures(context: String): Multi<String> {
        Log.debug("[FeatureToggleApi] Calling method: get url: /feature-toggles/$context/active")
        val mappedContext: Context = Context.valueOf(context.uppercase())
        return featureToggleRepository.getAllActive(mappedContext).onItem().transform { it.name }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    @Path("/feature-toggles")
    fun getAll(): Multi<FeatureToggleResponse> {
        Log.debug("[FeatureToggleApi] Calling method: get url: /feature-toggles")
        return featureToggleRepository.getAll().map { it.toResponse() }
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN, DefaultRoles.RELEASE_MANAGER, DefaultRoles.VIEWER)
    @Path("/feature-toggle/{id}")
    fun get(id: Long): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: get url: /feature-toggle/$id")
        return featureToggleRepository.getById(id).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("/feature-toggles")
    fun create(request: CreateFeatureToggleRequest): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: post url: /feature-toggles body: $request")
        return featureToggleService.create(request.key, request.name, request.description).onItem()
            .transform { RestResponse.ok(it.toResponse()) }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(DefaultRoles.ADMIN)
    @Path("/feature-toggle/{id}")
    fun partialUpdate(
        id: Long, updates: FeatureToggleFieldUpdateRequest
    ): Uni<RestResponse<FeatureToggleResponse>> {
        Log.debug("[FeatureToggleApi] Calling method: patch url: /feature-toggle/$id body: $updates")
        return featureToggleService.update(id, updates).onItem().transform {
            RestResponse.ok(it.toResponse())
        }.onFailure().transform { NotFoundException() }
    }

    fun FeatureToggle.toResponse(): FeatureToggleResponse {
        return FeatureToggleResponse(id, key, name, description, activation)
    }
}
