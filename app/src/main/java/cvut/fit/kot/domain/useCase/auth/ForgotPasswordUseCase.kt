package cvut.fit.kot.domain.useCase.auth

import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.data.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        request: ForgotPasswordRequest
    ): Result<Unit> = runCatching {
        authRepository.forgotPassword(request)
    }
}
