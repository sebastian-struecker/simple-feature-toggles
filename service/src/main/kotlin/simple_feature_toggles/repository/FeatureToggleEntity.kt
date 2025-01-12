package simple_feature_toggles.repository

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import simple_feature_toggles.FeatureToggle
import java.time.LocalDateTime


@Entity
@Table(name = "featuretoggleentity")
class FeatureToggleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false, unique = true) var key: String,
    @Column(nullable = false) var name: String,
    var description: String,
    @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
        name = "featureToggleEnvironmentActivation", joinColumns = [JoinColumn(name = "featuretoggleentity_id")]
    ) @MapKeyJoinColumn(name = "environmententity_key") @Column(
        name = "activation", nullable = true
    ) var environmentActivation: MutableMap<String, Boolean> = mutableMapOf(),
    @UpdateTimestamp val updatedAt: LocalDateTime,
    @CreationTimestamp @Column(nullable = false, updatable = false) val createdAt: LocalDateTime
) {
    constructor() : this(null, "", "", "", mutableMapOf(), LocalDateTime.now(), LocalDateTime.now())

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
