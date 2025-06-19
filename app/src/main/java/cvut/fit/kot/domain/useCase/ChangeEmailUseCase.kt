package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import javax.inject.Inject

class ChangeEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        request: ChangeEmailRequest
    ): Result<Unit> = runCatching {
        val auth = authRepository.changeEmail(request)
        sessionRepository.saveToken(auth.token)
    }
}