package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import retrofit2.HttpException
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun invoke(email: String, password: String, role: String): Result<Unit> {
        return try {
            val request = AuthRequest(email, password, role)
            val response = authRepository.signUp(request)
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    sessionRepository.save(token, role)
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
