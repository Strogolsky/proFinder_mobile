package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatMessagesUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    operator fun invoke(chatId: Long): Flow<ChatMessageResponse> =
        repo.observe(chatId)
}
