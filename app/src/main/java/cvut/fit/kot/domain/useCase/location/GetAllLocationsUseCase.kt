package cvut.fit.kot.domain.useCase.location

import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.repository.LocationRepository
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(): Result<List<LocationDto>> {
        return repository.getAll()
    }
}