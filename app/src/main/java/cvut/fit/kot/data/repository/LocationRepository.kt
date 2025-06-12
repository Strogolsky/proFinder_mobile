package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.remote.LocationAPI
import retrofit2.Response
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val api: LocationAPI
) {
    suspend fun getAll(): Response<List<LocationDto>> = api.getAll()
}