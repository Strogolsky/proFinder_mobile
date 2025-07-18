package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.SpecialistSearchRequest
import cvut.fit.kot.data.model.SpecialistSearchResponse
import cvut.fit.kot.data.remote.api.SearchApi
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: SearchApi
){
    suspend fun searchSpecialists(request: SpecialistSearchRequest): Result<List<SpecialistSearchResponse>> = runCatching {
        val response = api.searchSpecialists(request.query, request.location)

        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

}