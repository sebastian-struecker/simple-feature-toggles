package insta_toggles


import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeaturePanacheRepository : PanacheRepository<FeatureEntity>, FeatureRepository {

    override fun getAll(): Multi<Feature> {
        return Panache.withTransaction { listAll() }.toMulti().flatMap(Multi.createFrom()::iterable)
            .map { it.toDomain() }
    }

    override fun getAllActive(): Multi<Feature> {
        return Panache.withTransaction { find("isActive = ?1", true).list<FeatureEntity>() }.toMulti()
            .flatMap(Multi.createFrom()::iterable).map { it.toDomain() }
    }

    override fun getById(id: Long): Uni<Feature?> {
        return Panache.withTransaction { findById(id) ?: null }.map { it.toDomain() }
    }

    override fun getByName(name: String): Uni<Feature?> {
        return Panache.withTransaction { find("name = ?1", name)?.firstResult() }.map { it.toDomain() }
    }

    override fun create(name: String, description: String): Uni<Feature> {
        val entity = FeatureEntity()
        entity.name = name
        entity.description = description
        return Panache.withTransaction { persistAndFlush(entity) }.map { it.toDomain() }
    }

    override fun update(feature: Feature): Uni<Feature> {
        return Panache.withTransaction {
            update(
                "name = ?1, description = ?2, isActive= ?3 where id = ?4",
                feature.name,
                feature.description,
                feature.isActive,
                feature.id
            )
        }.map { feature }
    }

}


