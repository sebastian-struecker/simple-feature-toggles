package simple_feature_toggles.repository


import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import simple_feature_toggles.Environment
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.UpdateEnvironmentRequest


@ApplicationScoped
class EnvironmentPanacheRepository(
    val apiKeyPanacheRepository: ApiKeyPanacheRepository,
    val featureTogglePanacheRepository: FeatureTogglePanacheRepository
) : PanacheRepository<EnvironmentEntity>, EnvironmentRepository {

    override fun checkEnvironmentsExist(keys: List<String>): Uni<Boolean> {
        return Panache.withTransaction {
            find("key IN ?1", keys).count()
        }.map { it?.toInt() == keys.size }
    }

    override fun getAll(): Multi<Environment> {
        return Panache.withTransaction { listAll() }.toMulti().flatMap(Multi.createFrom()::iterable)
            .map { it.toDomain() }
    }

    override fun getById(id: Long): Uni<Environment> {
        return Panache.withTransaction { findById(id) }.onItem().ifNotNull()
            .transformToUni { it: EnvironmentEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Environment with id $id not found"))
    }

    override fun getByKey(key: String): Uni<Environment> {
        return Panache.withTransaction { find("key = ?1", key)?.firstResult() }.onItem().ifNotNull()
            .transformToUni { it: EnvironmentEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Environment with key $key not found"))
    }

    override fun create(createRequest: CreateEnvironmentRequest): Uni<Environment> {
        Environment.checkInputs(createRequest.key, createRequest.name)
        val entity = EnvironmentEntity.create(createRequest.key, createRequest.name)
        return Panache.withTransaction { persistAndFlush(entity).map { it.toDomain() } }
    }

    override fun update(id: Long, updates: UpdateEnvironmentRequest): Uni<Environment> {
        return Panache.withTransaction {
            findById(id).onItem().ifNull().failWith(NoSuchElementException("Environment with id $id not found")).map {
                it.apply {
                    if (updates.name?.isNotBlank() == true) {
                        name = updates.name
                    }
                }
            }
        }.chain { it: EnvironmentEntity -> Uni.createFrom().item(it.toDomain()) }

    }

    override fun removeById(id: Long): Uni<Unit> {
        return Panache.withTransaction {
            findById(id).onItem().ifNull().failWith(NoSuchElementException("Environment with id $id not found"))
                .onItem().ifNotNull().transformToUni { it ->
                    featureTogglePanacheRepository.onEnvironmentRemoval(it.key)
                }.onItem().transformToUni { it -> apiKeyPanacheRepository.onEnvironmentRemoval(it) }.onItem()
                .transformToUni { _ -> deleteById(id) }
        }.onItem().ifNotNull().transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }.onItem().ifNull()
            .failWith(NoSuchElementException("Environment with id $id not found"))
    }

    override fun removeAll(): Uni<Unit> {
        return Panache.withTransaction {
            deleteAll()
        }.chain { _ -> Uni.createFrom().voidItem().replaceWithUnit() }
    }

}
