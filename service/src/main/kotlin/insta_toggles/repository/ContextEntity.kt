package insta_toggles.repository

import insta_toggles.Context
import jakarta.persistence.*


@Entity
class ContextEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) var key: String,
    @Column(nullable = false) var name: String,
    @Column(nullable = false) var isActive: Boolean = false
) {
    constructor() : this(null, "", "", false)

    fun toDomain(): Context {
        return Context(id ?: throw IllegalStateException("ID cannot be null"), key, name, isActive)
    }
}
