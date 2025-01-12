package simple_feature_toggles.repository

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import simple_feature_toggles.Environment
import java.time.LocalDateTime


@Entity
@Table(name = "environmententity")
class EnvironmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true) var key: String,
    @Column(nullable = false) var name: String,
    @UpdateTimestamp val updatedAt: LocalDateTime,
    @CreationTimestamp @Column(nullable = false, updatable = false) val createdAt: LocalDateTime
) {
    constructor() : this(null, "", "", LocalDateTime.now(), LocalDateTime.now())

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
