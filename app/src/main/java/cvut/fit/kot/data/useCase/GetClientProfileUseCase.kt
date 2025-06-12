package cvut.fit.kot.data.useCase

import retrofit2.HttpException
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.repository.ClientRepository
import javax.inject.Inject

class GetClientProfileUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend fun execute(): Result<ClientResponse> = try {
        val response = repository.getProfile()
        if (response.isSuccessful) {
            response.body()?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("Empty body"))
        } else {
            Result.failure(HttpException(response))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
