package simple_feature_toggles.repository

import jakarta.persistence.*
import simple_feature_toggles.Environment


@Entity
class EnvironmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true) var key: String,
    @Column(nullable = false) var name: String,
) {
    constructor() : this(null, "", "")

    companion object {
        fun create(key: String, name: String): EnvironmentEntity {
            val environmentEntity = EnvironmentEntity()
            environmentEntity.key = key
            environmentEntity.name = name
            return environmentEntity
        }
    }

    fun toDomain(): Environment {
        return Environment(id ?: throw IllegalStateException("ID cannot be null"), key, name)
    }

}
