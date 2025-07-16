package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ServiceOfferingDto
import cvut.fit.kot.data.remote.api.ServiceApi
import retrofit2.HttpException
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val api: ServiceApi
) {
    suspend fun getAll(): Result<List<ServiceOfferingDto>> = runCatching {
        val response = api.getAll()

        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}