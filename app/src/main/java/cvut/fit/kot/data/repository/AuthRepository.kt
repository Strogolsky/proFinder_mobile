package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import cvut.fit.kot.data.remote.AuthApi
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {
    suspend fun signUp(request: AuthRequest): Response<AuthResponse> =
        api.signUp(request)

    suspend fun signIn(request: AuthRequest): Response<AuthResponse> =
        api.signIn(request)
}
