package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.SpecialistResponse
import cvut.fit.kot.data.repository.SpecialistRepository
import javax.inject.Inject

class GetSpecialistByIdUseCase @Inject constructor(
    private val repository: SpecialistRepository
) {

    suspend operator fun invoke(id: Long): Result<SpecialistResponse> {
        return repository.getById(id)
    }
}
