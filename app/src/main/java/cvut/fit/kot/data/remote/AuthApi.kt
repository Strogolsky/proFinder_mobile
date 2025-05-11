package cvut.fit.kot.data.remote

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/signUp")
    suspend fun signUp(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/signIn")
    suspend fun signIn(@Body request: AuthRequest): Response<AuthResponse>
}