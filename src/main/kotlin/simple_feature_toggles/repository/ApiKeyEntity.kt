package simple_feature_toggles.repository

import jakarta.persistence.*
import simple_feature_toggles.ApiKey


@Entity
class ApiKeyEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) var name: String,
    @Column(nullable = false, unique = true) var value: String,
    @ElementCollection @CollectionTable(
        name = "apiKeyEnvironmentActivation", joinColumns = [JoinColumn(name = "apikeyentity_id")]
    ) @MapKeyJoinColumn(name = "environmententity_key") @Column(
        name = "activation",
        nullable = true
    ) var environmentActivation: MutableMap<String, Boolean> = mutableMapOf(),
) {
    constructor() : this(null, "", "", mutableMapOf())

    companion object {
        fun create(name: String, value: String, environmentActivation: MutableMap<String, Boolean>): ApiKeyEntity {
            val entity = ApiKeyEntity()
            entity.name = name
            entity.value = value
            entity.environmentActivation = environmentActivation
            return entity
        }
    }

    fun toDomain(): ApiKey {
        return ApiKey(id ?: throw IllegalStateException("ID cannot be null"), name, value, environmentActivation)
    }
}
