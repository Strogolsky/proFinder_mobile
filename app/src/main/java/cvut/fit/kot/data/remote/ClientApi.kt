package cvut.fit.kot.data.remote

import cvut.fit.kot.data.model.ClientResponse
import retrofit2.Response
import retrofit2.http.GET

interface ClientApi {
    @GET("client/me")
    suspend fun getProfile(): Response<ClientResponse>
}