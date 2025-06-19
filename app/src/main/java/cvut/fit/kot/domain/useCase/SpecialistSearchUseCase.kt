package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.SpecialistSearchRequest
import cvut.fit.kot.data.model.SpecialistSearchResponse
import cvut.fit.kot.data.repository.SearchRepository
import javax.inject.Inject

class SpecialistSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        request: SpecialistSearchRequest
    ): Result<List<SpecialistSearchResponse>> {
        return repository.searchSpecialists(request)
    }
}