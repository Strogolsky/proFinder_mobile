package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.repository.ClientRepository
import retrofit2.HttpException
import javax.inject.Inject

class UpdateClientProfileUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(request: ClientRequest): Result<ClientResponse> {
        return repository.updateProfile(request)
    }

}