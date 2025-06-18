package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.repository.LocationRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend fun invoke(): Result<List<LocationDto>> = runCatching {
        val response = repository.getAll()

        if (response.isSuccessful) {
            response.body()
                ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}