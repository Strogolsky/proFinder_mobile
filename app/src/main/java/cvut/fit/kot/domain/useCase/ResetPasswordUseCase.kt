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
    suspend operator fun invoke(
        request: ResetPasswordRequest
    ): Result<Unit> = runCatching {
        val auth = authRepository.resetPassword(request)
        sessionRepository.saveToken(auth.token)
    }
}