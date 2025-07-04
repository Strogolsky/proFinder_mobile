package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.OrderDto
import cvut.fit.kot.data.remote.api.OrderApi
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
){
    suspend fun getByClient(): Result<List<OrderDto>> = runCatching {
        val response = api.getByClient()
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}