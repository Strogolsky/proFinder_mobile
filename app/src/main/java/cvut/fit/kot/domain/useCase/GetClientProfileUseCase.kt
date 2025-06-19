package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.repository.ClientRepository
import javax.inject.Inject

class GetClientProfileUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(): ClientResponse {
        return repository.getProfile()
    }
}
