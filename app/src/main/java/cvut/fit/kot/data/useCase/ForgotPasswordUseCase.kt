package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.data.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun execute(request: ForgotPasswordRequest): Result<Unit> {
        return try {
            val response = authRepository.forgotPassword(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
