package cvut.fit.kot.domain.useCase.chat

import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.repository.ChatRepository
import javax.inject.Inject

class GetChatsProfileUseCase @Inject constructor(
    private val repository: ChatRepository
){
    suspend operator fun invoke(): Result<List<ChatResponse>> {
        return repository.getChatsProfile()
    }
}