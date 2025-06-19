package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.remote.api.LocationApi
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val api: LocationApi
) {
    suspend fun getAll(): Result<List<LocationDto>> = runCatching {
        val response = api.getAll()

        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}