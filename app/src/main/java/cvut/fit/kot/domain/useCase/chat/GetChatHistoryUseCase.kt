package cvut.fit.kot.domain.useCase.chat

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.repository.ChatRepository
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: Long): Result<List<ChatMessageResponse>> {
        return repository.getHistory(chatId)
    }
}