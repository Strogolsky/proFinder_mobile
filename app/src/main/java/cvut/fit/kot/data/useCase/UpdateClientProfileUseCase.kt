package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.repository.ClientRepository
import retrofit2.HttpException
import javax.inject.Inject

class UpdateClientProfileUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend fun execute(request: ClientRequest): ClientResponse {
        val response = repository.updateProfile(request)

        if (response.isSuccessful) {
            return response.body()
                ?: throw IllegalStateException("Error response")
        } else {
            throw HttpException(response)
        }
    }

}