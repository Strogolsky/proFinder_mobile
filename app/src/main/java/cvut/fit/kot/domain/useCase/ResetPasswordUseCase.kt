package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ResetPasswordRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import retrofit2.HttpException
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun invoke(request: ResetPasswordRequest): Result<Unit> {
        return try {
            val response = authRepository.resetPassword(request)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    sessionRepository.saveToken(token)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("No token in response"))
                }
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}