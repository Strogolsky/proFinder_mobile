package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ServiceOfferingDto
import cvut.fit.kot.data.repository.ServiceRepository
import javax.inject.Inject

class GetAllServiceOfferingUseCase @Inject constructor(
    private val repository: ServiceRepository
) {
    suspend operator fun invoke(): Result<List<ServiceOfferingDto>> =
        repository.getAll()
}
