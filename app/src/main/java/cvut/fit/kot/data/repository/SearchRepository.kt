package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.SpecialistSearchRequest
import cvut.fit.kot.data.model.SpecialistSearchResponse
import cvut.fit.kot.data.remote.SearchApi
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: SearchApi
){
    suspend fun searchSpecialists(request: SpecialistSearchRequest): Response<List<SpecialistSearchResponse>>
        = api.searchSpecialists(request.query, request.location)

}