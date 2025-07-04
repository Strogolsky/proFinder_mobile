package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.OrderDto
import retrofit2.Response
import retrofit2.http.GET

interface OrderApi {
    @GET("order/client")
    suspend fun getByClient(): Response<List<OrderDto>>
}