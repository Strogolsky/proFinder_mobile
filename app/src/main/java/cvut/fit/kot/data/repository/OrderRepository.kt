package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.OrderResponse
import cvut.fit.kot.data.remote.api.OrderApi
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
){
    suspend fun getByClient(): Result<List<OrderResponse>> = runCatching {
        val response = api.getByClient()
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getByOrderId(orderId: Long) : Result<OrderResponse> = runCatching {
        val response = api.getByOrderId(orderId)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun cancel(orderId: Long) : Result<OrderResponse> = runCatching {
        val response = api.cancel(orderId)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
    suspend fun create(request: OrderRequest) : Result<OrderResponse> = runCatching {
        val response = api.create(request)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun update(orderId: Long, request: OrderRequest) : Result<OrderResponse> = runCatching {
        val response = api.update(orderId, request)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }




}