package cvut.fit.kot.domain.useCase.auth

import cvut.fit.kot.data.model.ChangePasswordRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        request: ChangePasswordRequest
    ): Result<Unit> = runCatching {
        val auth = authRepository.changePassword(request)
        sessionRepository.saveToken(auth.token)
    }
}