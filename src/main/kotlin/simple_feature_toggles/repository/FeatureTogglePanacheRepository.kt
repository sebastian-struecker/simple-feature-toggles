package simple_feature_toggles.repository


import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import simple_feature_toggles.ContextName
import simple_feature_toggles.FeatureToggle
import simple_feature_toggles.FeatureToggleRepository
import simple_feature_toggles.api.models.UpdateFeatureToggleRequest

@ApplicationScoped
class FeatureTogglePanacheRepository : PanacheRepository<FeatureToggleEntity>, FeatureToggleRepository {

    override fun getAll(): Multi<FeatureToggle> {
        return Panache.withTransaction { listAll() }.toMulti().flatMap(Multi.createFrom()::iterable)
            .map { it.toDomain() }
    }

    override fun getAllActive(contextName: ContextName): Multi<FeatureToggle> {
        return Panache.withTransaction {
            find(
                "#FeatureToggleEntity.findWithActiveContext", Parameters.with("contextKey", contextName.toString())
            ).list<FeatureToggleEntity>()
        }.toMulti().flatMap(Multi.createFrom()::iterable).map { it.toDomain() }
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

    override fun create(key: String, name: String, description: String): Uni<FeatureToggle> {
        FeatureToggle.checkInputs(key, description)
        val entity = FeatureToggleEntity()
        entity.key = key
        entity.name = name
        entity.description = description
        entity.contexts = createContexts()
        return Panache.withTransaction { persistAndFlush(entity).map { it.toDomain() } }
    }

    override fun update(id: Long, updates: UpdateFeatureToggleRequest): Uni<FeatureToggle> {
        return Panache.withTransaction {
            findById(id).onItem().ifNull().failWith(NoSuchElementException("Feature with id $id not found")).map {
                it.apply {
                    if (updates.name?.isNotBlank() == true) {
                        name = updates.name!!
                    }
                    if (updates.description?.isNotBlank() == true) {
                        description = updates.description!!
                    }
                    if (updates.contexts?.isNotEmpty() == true) {
                        updates.contexts?.forEach { updated ->
                            contexts.firstOrNull { it.key == updated.key }?.let {
                                it.isActive = updated.isActive
                            }
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

    fun createContexts(): MutableList<ContextEntity> {
        return mutableListOf(
            ContextEntity.create(ContextName.testing), ContextEntity.create(ContextName.production)
        )
    }

}


