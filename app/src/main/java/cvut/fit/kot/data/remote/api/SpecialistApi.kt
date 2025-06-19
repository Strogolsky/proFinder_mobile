package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.SpecialistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpecialistApi {
    @GET("specialist/{specialistId}")
    suspend fun getById(@Path("specialistId") specialistId: Long): Response<SpecialistResponse>
}