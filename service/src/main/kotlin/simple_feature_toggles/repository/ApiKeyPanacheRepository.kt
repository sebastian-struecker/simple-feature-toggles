package simple_feature_toggles.repository


import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import simple_feature_toggles.ApiKey
import simple_feature_toggles.ApiKeyRepository
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.api.models.CreateApiKeyRequest
import simple_feature_toggles.api.models.UpdateApiKeyRequest

@ApplicationScoped
class ApiKeyPanacheRepository(private val environmentRepository: EnvironmentRepository) :
    PanacheRepository<ApiKeyEntity>, ApiKeyRepository {

    override fun getAll(): Uni<List<ApiKey>> {
        return Panache.withTransaction { listAll() }.map { it.map { it.toDomain() } }
    }

    override fun getById(id: Long): Uni<ApiKey> {
        return Panache.withTransaction { findById(id) }.onItem().ifNotNull()
            .transformToUni { it: ApiKeyEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Api key with id $id not found"))
    }

    override fun create(createRequest: CreateApiKeyRequest): Uni<ApiKey> {
        if (createRequest.environmentActivations.isEmpty()) {
            val entity = ApiKeyEntity.create(createRequest.name, mutableMapOf())
            return Panache.withTransaction {
                persistAndFlush(entity).map {
                    it.toDomain()
                }
            }
        }
        return environmentRepository.checkEnvironmentsExist(createRequest.environmentActivations.map { it.environmentKey })
            .flatMap { allExist ->
                if (!allExist) {
                    Uni.createFrom().failure(IllegalArgumentException("One or more environments do not exist"))
                } else {
                    val mappedEnvironmentActivation = createRequest.environmentActivations.map { entry ->
                        entry.environmentKey to entry.isActive
                    }.toMap().toMutableMap()
                    val entity = ApiKeyEntity.create(createRequest.name, mappedEnvironmentActivation)
                    Panache.withTransaction {
                        persistAndFlush(entity).map { it.toDomain() }
                    }
                }
            }
    }

    override fun update(id: Long, updates: UpdateApiKeyRequest): Uni<ApiKey> {
        return Panache.withTransaction {
            findById(id).onItem().ifNull().failWith(NoSuchElementException("Api key with id $id not found")).map {
                it.apply {
                    if (updates.name?.isNotBlank() == true) {
                        name = updates.name
                    }
                    if (updates.environmentActivations?.isNotEmpty() == true) {
                        updates.environmentActivations.forEach { entry ->
                            environmentActivation[entry.environmentKey] = entry.isActive
                        }
                    }
                }
            }
        }.chain { it: ApiKeyEntity -> Uni.createFrom().item(it.toDomain()) }

    }

    override fun removeById(id: Long): Uni<Unit> {
        return Panache.withTransaction {
            deleteById(id)
        }.onItem().ifNotNull().transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }.onItem().ifNull()
            .failWith(NoSuchElementException("Api key with id $id not found"))
    }

    override fun removeAll(): Uni<Unit> {
        return Panache.withTransaction {
            deleteAll()
        }.chain { _ -> Uni.createFrom().voidItem().replaceWithUnit() }
    }

    override fun onEnvironmentRemoval(environmentKey: String): Uni<String> {
        return Panache.withTransaction {
            listAll()
        }.toMulti().flatMap(Multi.createFrom()::iterable).filter {
            it.environmentActivation.entries.any { entry -> entry.key == environmentKey }
        }.map {
            it.apply {
                environmentActivation.remove(environmentKey)
            }
        }.toUni().map { _ -> environmentKey }
    }

}


