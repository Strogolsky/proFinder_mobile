package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {
    @GET("order/client")
    suspend fun getByClient(): Response<List<OrderResponse>>

    @GET("order/{orderId}")
    suspend fun getByOrderId(@Path("orderId") orderId: Long): Response<OrderResponse>

    @PUT("order/{orderId}/cancel")
    suspend fun cancel(@Path("orderId") orderId: Long): Response<OrderResponse>

    @POST("/order")
    suspend fun create(@Body request: OrderRequest): Response<OrderResponse>

    @PUT("/order/{orderId}")
    suspend fun update(@Path("orderId") orderId: Long, @Body request: OrderRequest): Response<OrderResponse>


}