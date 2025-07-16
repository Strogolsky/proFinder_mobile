package cvut.fit.kot.domain.useCase.auth

import cvut.fit.kot.data.model.AuthRequest
import cvut.fit.kot.data.repository.AuthRepository
import cvut.fit.kot.data.repository.SessionRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        role: String
    ): Result<Unit> = runCatching {
        val auth = authRepository.signUp(AuthRequest(email, password, role))
        sessionRepository.save(auth.token, role)
    }
}
