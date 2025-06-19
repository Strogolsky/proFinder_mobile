package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.SpecialistSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search/specialist")
    suspend fun searchSpecialists(
        @Query("query") query: String,
        @Query("location") location: String? = null
    ): Response<List<SpecialistSearchResponse>>
}