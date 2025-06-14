package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import retrofit2.HttpException
import javax.inject.Inject

class ChangeEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun execute(request: ChangeEmailRequest): Result<Unit> {
        return try {
            val response = authRepository.changeEmail(request)
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