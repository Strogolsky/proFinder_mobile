package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.remote.ClientApi
import retrofit2.Response
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val api: ClientApi
) {
    suspend fun getProfile(): Response<ClientResponse> = api.getProfile()
}