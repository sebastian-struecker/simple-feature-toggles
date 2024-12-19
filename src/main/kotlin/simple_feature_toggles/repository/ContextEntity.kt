package simple_feature_toggles.repository

import simple_feature_toggles.Context
import simple_feature_toggles.ContextName
import jakarta.persistence.*


@Entity
class ContextEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) var key: String,
    @Column(nullable = false) var name: String,
    @Column(nullable = false) var isActive: Boolean = false,
) {
    constructor() : this(null, "", "", false)

    companion object {
        fun create(contextName: ContextName): ContextEntity {
            val contextEntity = ContextEntity()
            contextEntity.key = contextName.toString()
            contextEntity.name = contextName.toString()
            return contextEntity
        }
    }

    fun toDomain(): Context {
        return Context(id ?: throw IllegalStateException("ID cannot be null"), key, name, isActive)
    }

}
