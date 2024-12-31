package simple_feature_toggles.repository

import jakarta.persistence.*
import simple_feature_toggles.FeatureToggle


@Entity
class FeatureToggleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false, unique = true) var key: String,
    @Column(nullable = false) var name: String,
    var description: String,
    @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
        name = "featureToggleEnvironmentActivation", joinColumns = [JoinColumn(name = "featuretoggleentity_id")]
    ) @MapKeyJoinColumn(name = "environmententity_key") @Column(
        name = "activation",
        nullable = true
    ) var environmentActivation: MutableMap<String, Boolean> = mutableMapOf(),
) {
    constructor() : this(null, "", "", "", mutableMapOf())

    companion object {
        fun create(
            key: String, name: String, description: String, environmentActivation: MutableMap<String, Boolean>
        ): FeatureToggleEntity {
            val entity = FeatureToggleEntity()
            entity.key = key
            entity.name = name
            entity.description = description
            entity.environmentActivation = environmentActivation
            return entity
        }
    }

    fun toDomain(): FeatureToggle {
        return FeatureToggle(
            id ?: throw IllegalStateException("ID cannot be null"), key, name, description, environmentActivation
        )
    }

}
