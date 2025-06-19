package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.repository.ChatRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: Long): Result<List<ChatMessageResponse>> {
        return repository.getHistory(chatId)
    }
}