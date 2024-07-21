package insta_toggles.repository


import insta_toggles.ContextName
import insta_toggles.FeatureToggle
import insta_toggles.FeatureToggleRepository
import insta_toggles.api.models.FeatureToggleUpdateRequest
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

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
        return Panache.withTransaction { findByKey(key) }.onItem().ifNotNull()
            .transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with name $key not found"))
    }


    override fun create(key: String, name: String, description: String): Uni<FeatureToggle> {
        FeatureToggle.checkInputs(key, description)
        val entity = FeatureToggleEntity()
        entity.key = key
        entity.name = name
        entity.description = description
        return Panache.withTransaction { persistAndFlush(entity).map { it.toDomain() } }
    }

    override fun update(id: Long, updates: FeatureToggleUpdateRequest): Uni<FeatureToggle> {
        return Panache.withTransaction {
            findById(id).map { it ->
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
        }.onItem().ifNotNull().transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }
            .onItem().ifNull().failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun removeById(id: Long): Uni<Unit> {
        return Panache.withTransaction { deleteById(id) }.onItem().ifNotNull()
            .transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun removeAll(): Uni<Unit> {
        return Panache.withTransaction { deleteAll() }.onItem()
            .transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }
    }

    private fun findByKey(key: String): Uni<FeatureToggleEntity>? = find("key = ?1", key)?.firstResult()

}


