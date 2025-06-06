package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.repository.SessionRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend fun execute() {
        sessionRepository.clear();
    }

}