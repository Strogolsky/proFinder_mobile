package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.model.AuthResponse
import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.model.ChangePasswordRequest
import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.data.model.ResetPasswordRequest
import cvut.fit.kot.data.remote.api.AuthApi
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {
    suspend fun signUp(request: AuthRequest): Response<AuthResponse> =
        api.signUp(request)

    suspend fun signIn(request: AuthRequest): Response<AuthResponse> =
        api.signIn(request)

    suspend fun changePassword(request: ChangePasswordRequest): Response<AuthResponse> =
        api.changePassword(request)

    suspend fun forgotPassword(request: ForgotPasswordRequest): Response<Unit> =
        api.forgotPassword(request)

    suspend fun resetPassword(request: ResetPasswordRequest): Response<AuthResponse> =
        api.resetPassword(request)

    suspend fun changeEmail(request: ChangeEmailRequest): Response<AuthResponse> =
        api.changeEmail(request);
}
