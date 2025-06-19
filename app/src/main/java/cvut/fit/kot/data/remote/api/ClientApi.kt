package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.ClientResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ClientApi {
    @GET("client/me")
    suspend fun getProfile(): Response<ClientResponse>

    @PUT("client/me")
    suspend fun updateProfile(@Body request: ClientRequest): Response<ClientResponse>
}