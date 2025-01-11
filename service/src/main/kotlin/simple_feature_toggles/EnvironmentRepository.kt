package simple_feature_toggles

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import simple_feature_toggles.api.models.CreateEnvironmentRequest
import simple_feature_toggles.api.models.UpdateEnvironmentRequest

interface EnvironmentRepository {

    fun checkEnvironmentsExist(keys: List<String>): Uni<Boolean>

    fun getAll(): Multi<Environment>

    fun getById(id: Long): Uni<Environment>

    fun getByKey(key: String): Uni<Environment>

    fun create(createRequest: CreateEnvironmentRequest): Uni<Environment>

    fun update(id: Long, updates: UpdateEnvironmentRequest): Uni<Environment>

    fun removeById(id: Long): Uni<Unit>

    fun removeAll(): Uni<Unit>

}
