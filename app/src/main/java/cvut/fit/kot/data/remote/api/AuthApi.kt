package cvut.fit.kot.data.remote.api

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.model.ChangePasswordRequest
import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.data.model.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("auth/signUp")
    suspend fun signUp(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/signIn")
    suspend fun signIn(@Body request: AuthRequest): Response<AuthResponse>

    @PUT("auth/password/change")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<AuthResponse>

    @PUT("auth/password/forgot")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>

    @PUT("auth/password/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<AuthResponse>

    @PUT("auth/email/change")
    suspend fun changeEmail(@Body request: ChangeEmailRequest): Response<AuthResponse>
}