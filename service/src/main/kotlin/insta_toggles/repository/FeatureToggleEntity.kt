package insta_toggles.repository

import insta_toggles.ContextName
import insta_toggles.FeatureToggle
import jakarta.persistence.*


@Entity
@NamedQueries(
    NamedQuery(
        name = "FeatureToggleEntity.findWithActiveContext",
        query = """
            SELECT f FROM FeatureToggleEntity f 
            JOIN f.contexts c 
            WHERE c.key = :contextKey 
            AND c.isActive = true
        """
    )
)
class FeatureToggleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) var key: String,
    @Column(nullable = false) var name: String,
    var description: String,
    @OneToMany(
        fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true
    ) @JoinColumn(name = "featuretoggleentity_id") var contexts: MutableList<ContextEntity> = mutableListOf()
) {
    constructor() : this(null, "", "", "", mutableListOf())

    init {
        if (contexts.isEmpty()) {
            contexts.addAll(defaultContexts())
        }
    }

    fun toDomain(): FeatureToggle {
        return FeatureToggle(id ?: throw IllegalStateException("ID cannot be null"),
            key,
            name,
            description,
            contexts.map { it.toDomain() })
    }

    @Transient
    private fun defaultContexts(): MutableList<ContextEntity> {
        return mutableListOf(createContextEntity(ContextName.testing), createContextEntity(ContextName.production))
    }

    @Transient
    private fun createContextEntity(contextName: ContextName): ContextEntity {
        val contextEntity = ContextEntity()
        contextEntity.key = contextName.toString()
        contextEntity.name = contextName.toString()
        return contextEntity
    }

}
