package insta_toggles

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "feature_entity")
class FeatureEntity {
    @Id
    @GeneratedValue
    var id: Long? = null
    lateinit var name: String
    lateinit var description: String
    var isActive: Boolean = false

    fun toDomain(): Feature {
        return Feature(id!!, name, description, isActive)
    }
}
