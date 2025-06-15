package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.SpecialistSearchRequest
import cvut.fit.kot.data.model.SpecialistSearchResponse
import cvut.fit.kot.data.repository.SearchRepository
import retrofit2.HttpException
import javax.inject.Inject

class SpecialistSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun execute(
        request: SpecialistSearchRequest
    ): Result<List<SpecialistSearchResponse>> = try {
        val response = repository.searchSpecialists(request)

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