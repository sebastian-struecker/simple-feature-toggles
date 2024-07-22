package insta_toggles.repository

import insta_toggles.ContextName
import insta_toggles.FeatureToggle
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.NamedQueries
import jakarta.persistence.NamedQuery
import jakarta.persistence.OneToMany
import jakarta.persistence.Transient
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction


@Entity
@NamedQueries(
    NamedQuery(
        name = "FeatureToggleEntity.findWithActiveContext", query = """
            SELECT f FROM FeatureToggleEntity f 
            JOIN f.contexts c 
            WHERE c.key = :contextKey 
            AND c.isActive = true
        """
    )
)
class FeatureToggleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var key: String,
    @Column(nullable = false)
    var name: String,
    var description: String,
    @OneToMany(
        fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true
    )
    @JoinColumn(name = "featuretoggleentity_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var contexts: MutableList<ContextEntity>
) {
    constructor() : this(null, "", "", "", mutableListOf())

    init {
        if (contexts.isEmpty()) {
            contexts = defaultContexts()
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
