package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.SpecialistResponse
import cvut.fit.kot.data.repository.SpecialistRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetSpecialistByIdUseCase @Inject constructor(
    private val repository: SpecialistRepository
) {

    suspend operator fun invoke(id: Long): Result<SpecialistResponse> = runCatching {
        val response = repository.getById(id)

        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty body")
        } else {
            throw HttpException(response)
        }
    }
}
