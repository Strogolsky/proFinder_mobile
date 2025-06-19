package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.remote.api.ClientApi
import retrofit2.HttpException
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val api: ClientApi
) {
    suspend fun getProfile(): ClientResponse {
        val response = api.getProfile()
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun updateProfile(request: ClientRequest): ClientResponse {
        val response = api.updateProfile(request)
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}