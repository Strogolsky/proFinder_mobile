package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.SpecialistResponse
import cvut.fit.kot.data.remote.api.SpecialistApi
import retrofit2.Response
import javax.inject.Inject

class SpecialistRepository @Inject constructor(
    private val api: SpecialistApi
) {
    suspend fun getById(specialistId: Long): Response<SpecialistResponse> =
        api.getById(specialistId)
}