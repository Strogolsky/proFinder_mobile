package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.model.ChangePasswordRequest
import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.data.model.ResetPasswordRequest
import cvut.fit.kot.data.remote.api.AuthApi
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {
    suspend fun signUp(request: AuthRequest): AuthResponse {
        val response = api.signUp(request)
        if (!response.isSuccessful) throw HttpException(response)

        return response.body()
            ?: throw IllegalStateException("Empty body in signUp() response")
    }

    suspend fun signIn(request: AuthRequest): AuthResponse {
        val response = api.signIn(request)
        if (!response.isSuccessful) throw HttpException(response)

        return response.body()
            ?: throw IllegalStateException("Empty body in signIn() response")
    }

    suspend fun changePassword(request: ChangePasswordRequest): Response<AuthResponse> =
        api.changePassword(request)

    suspend fun forgotPassword(request: ForgotPasswordRequest): Response<Unit> =
        api.forgotPassword(request)

    suspend fun resetPassword(request: ResetPasswordRequest): Response<AuthResponse> =
        api.resetPassword(request)

    suspend fun changeEmail(request: ChangeEmailRequest): Response<AuthResponse> =
        api.changeEmail(request);
}
