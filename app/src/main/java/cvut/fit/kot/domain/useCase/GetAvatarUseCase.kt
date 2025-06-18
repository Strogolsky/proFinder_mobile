package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.repository.FileRepository
import javax.inject.Inject

class GetAvatarUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(userId: Long): ByteArray? =
        repository.getAvatar(userId)
}