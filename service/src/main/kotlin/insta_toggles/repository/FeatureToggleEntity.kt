package insta_toggles.repository

import insta_toggles.Context
import insta_toggles.FeatureToggle
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.NamedQueries
import jakarta.persistence.NamedQuery
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode


@Entity(name = "feature_toggle_entity")
@NamedQueries(
    NamedQuery(
        name = "FeatureToggleEntity.getAllActive",
        query = "from feature_toggle_entity where activation[?1] = ?2"
    ),
)
class FeatureToggleEntity {
    @Id
    @GeneratedValue
    var id: Long? = null
    lateinit var key: String
    lateinit var name: String
    lateinit var description: String

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    val activation: MutableMap<Context, Boolean> = mutableMapOf(Context.TESTING to false, Context.PRODUCTION to false)

    fun toDomain(): FeatureToggle {
        return FeatureToggle(id!!, key, name, description, activation)
    }

}
