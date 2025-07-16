package cvut.fit.kot.domain.useCase.auth

import cvut.fit.kot.data.repository.SessionRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend fun invoke() {
        sessionRepository.clear();
    }

}