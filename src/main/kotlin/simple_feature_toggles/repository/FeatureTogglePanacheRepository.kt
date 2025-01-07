package simple_feature_toggles.repository


import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import simple_feature_toggles.EnvironmentRepository
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.FeatureToggleRepository
import simple_feature_toggles.api.models.CreateFeatureToggleRequest
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest

@ApplicationScoped
class FeatureTogglePanacheRepository(private val environmentRepository: EnvironmentRepository) :
    PanacheRepository<FeatureToggleEntity>, FeatureToggleRepository {

    override fun getAll(): Multi<FeatureToggle> {
        return Panache.withTransaction { listAll() }.toMulti().flatMap(Multi.createFrom()::iterable)
            .map { it.toDomain() }
    }

    override fun getAllActive(environmentKey: String): Multi<FeatureToggle> {
        return Panache.withTransaction {
            listAll()
        }.toMulti().flatMap(Multi.createFrom()::iterable).filter {
            it.environmentActivation.entries.any { entry -> entry.key == environmentKey && entry.value }
        }.map { it.toDomain() }
    }

    override fun getById(id: Long): Uni<FeatureToggle> {
        return Panache.withTransaction { findById(id) }.onItem().ifNotNull()
            .transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun getByKey(key: String): Uni<FeatureToggle> {
        return Panache.withTransaction { find("key = ?1", key)?.firstResult() }.onItem().ifNotNull()
            .transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with key $key not found"))
    }

    override fun create(createRequest: CreateFeatureToggleRequest): Uni<FeatureToggle> {
        FeatureToggle.checkInputs(createRequest.key, createRequest.name)
        if (createRequest.environmentActivations.isEmpty()) {
            val entity = FeatureToggleEntity.create(
                createRequest.key, createRequest.name, createRequest.description, mutableMapOf()
            )
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
                    val entity = FeatureToggleEntity.create(
                        createRequest.key, createRequest.name, createRequest.description, mappedEnvironmentActivation
                    )
                    Panache.withTransaction {
                        persistAndFlush(entity).map { it.toDomain() }
                    }
                }
            }
    }

    override fun update(id: Long, updates: UpdateFeatureToggleRequest): Uni<FeatureToggle> {
        return Panache.withTransaction {
            findById(id).onItem().ifNull().failWith(NoSuchElementException("Feature with id $id not found")).map {
                it.apply {
                    if (updates.name?.isNotBlank() == true) {
                        name = updates.name
                    }
                    if (updates.description?.isNotBlank() == true) {
                        description = updates.description
                    }
                    if (updates.environmentActivations?.isNotEmpty() == true) {
                        updates.environmentActivations.forEach { entry ->
                            environmentActivation[entry.environmentKey] = entry.isActive
                        }
                    }
                }
            }
        }.chain { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }
    }

    override fun removeById(id: Long): Uni<Unit> {
        return Panache.withTransaction {
            deleteById(id)
        }.onItem().ifNotNull().transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun removeAll(): Uni<Unit> {
        return Panache.withTransaction {
            deleteAll()
        }.chain { _ -> Uni.createFrom().voidItem().replaceWithUnit() }
    }

}


