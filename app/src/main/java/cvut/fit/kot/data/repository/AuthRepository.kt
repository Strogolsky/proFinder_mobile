package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.SignUpRequest
import cvut.fit.kot.data.model.SignUpResponse
import cvut.fit.kot.data.remote.ApiClient
import retrofit2.Response

class AuthRepository {
    suspend fun signUp(request: SignUpRequest): Response<SignUpResponse> {
        return ApiClient.authApi.signUp(request)
    }
}