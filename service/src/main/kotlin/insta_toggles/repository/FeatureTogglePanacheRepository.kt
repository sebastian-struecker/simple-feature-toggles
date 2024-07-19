package insta_toggles.repository


import insta_toggles.Context
import insta_toggles.FeatureToggle
import insta_toggles.FeatureToggleRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
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

    override fun getAllActive(context: Context): Multi<FeatureToggle> {
        return Panache.withTransaction {
            find(
                "#FeatureToggleEntity.getAllActive",
                context,
                true,
            ).list<FeatureToggleEntity>()
        }.toMulti().flatMap(Multi.createFrom()::iterable).map { it.toDomain() }
    }

    override fun getById(id: Long): Uni<FeatureToggle> {
        return Panache.withTransaction { findById(id) }.onItem().ifNotNull()
            .transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun getByKey(key: String): Uni<FeatureToggle> {
        return Panache.withTransaction { find("name = ?1", key)?.firstResult() }.onItem().ifNotNull()
            .transformToUni { it: FeatureToggleEntity -> Uni.createFrom().item(it.toDomain()) }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with name $key not found"))
    }

    override fun create(key: String, name: String, description: String): Uni<FeatureToggle> {
        val entity = FeatureToggleEntity()
        entity.key = key
        entity.name = name
        entity.description = description
        return Panache.withTransaction { persistAndFlush(entity) }.map { it.toDomain() }
    }

    override fun update(featureToggle: FeatureToggle): Uni<FeatureToggle> {
        return Panache.withTransaction {
            update(
                "name = ?1, description = ?2, activation= ?3 where id = ?4",
                featureToggle.name,
                featureToggle.description,
                featureToggle.activation,
                featureToggle.id
            )
        }.map { featureToggle }
    }

    override fun removeById(id: Long): Uni<Unit> {
        return Panache.withTransaction { delete("id = ?1", id) }.onItem().ifNotNull()
            .transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }.onItem().ifNull()
            .failWith(NoSuchElementException("Feature with id $id not found"))
    }

    override fun removeAll(): Uni<Unit> {
        return Panache.withTransaction { deleteAll() }.onItem()
            .transformToUni { _ -> Uni.createFrom().voidItem().replaceWithUnit() }
    }

}


