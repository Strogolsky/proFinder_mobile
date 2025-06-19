package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.LocationDto
import retrofit2.Response
import retrofit2.http.GET

interface LocationApi {
    @GET("/location")
    suspend fun getAll(): Response<List<LocationDto>>
}