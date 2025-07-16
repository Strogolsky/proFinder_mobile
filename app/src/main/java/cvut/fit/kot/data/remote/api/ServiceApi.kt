package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.ServiceOfferingDto
import retrofit2.Response
import retrofit2.http.GET

interface ServiceApi {
    @GET("/service")
    suspend fun getAll(): Response<List<ServiceOfferingDto>>
}