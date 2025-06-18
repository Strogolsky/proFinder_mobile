package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.remote.api.LocationApi
import retrofit2.Response
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val api: LocationApi
) {
    suspend fun getAll(): Response<List<LocationDto>> = api.getAll()
}