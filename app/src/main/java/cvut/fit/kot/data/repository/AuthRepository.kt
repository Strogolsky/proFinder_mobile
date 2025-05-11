package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import cvut.fit.kot.data.remote.ApiClient
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    suspend fun signUp(request: AuthRequest): Response<AuthResponse> {
        return ApiClient.authApi.signUp(request)
    }

    suspend fun signIn(request: AuthRequest): Response<AuthResponse> =
        ApiClient.authApi.signIn(request)
}
