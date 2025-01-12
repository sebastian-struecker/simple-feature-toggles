package simple_feature_toggles.repository

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import simple_feature_toggles.ApiKey
import java.time.LocalDateTime


@Entity
@Table(name = "apikeyentity")
class ApiKeyEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) var name: String,
    @Column(nullable = false, unique = true) var value: String,
    @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
        name = "apiKeyEnvironmentActivation", joinColumns = [JoinColumn(name = "apikeyentity_id")]
    ) @MapKeyJoinColumn(name = "environmententity_key") @Column(
        name = "activation", nullable = true
    ) var environmentActivation: MutableMap<String, Boolean> = mutableMapOf(),
    @UpdateTimestamp val updatedAt: LocalDateTime,
    @CreationTimestamp @Column(nullable = false, updatable = false) val createdAt: LocalDateTime
) {
    constructor() : this(null, "", "", mutableMapOf(), LocalDateTime.now(), LocalDateTime.now())

    companion object {
        fun create(name: String, environmentActivation: MutableMap<String, Boolean>): ApiKeyEntity {
            val entity = ApiKeyEntity()
            entity.name = name
            entity.value = ApiKey.generateValue()
            entity.environmentActivation = environmentActivation
            return entity
        }
    }

    fun toDomain(): ApiKey {
        return ApiKey(id ?: throw IllegalStateException("ID cannot be null"), name, value, environmentActivation)
    }
}
