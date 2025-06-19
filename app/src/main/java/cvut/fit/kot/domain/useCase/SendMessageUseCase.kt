package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    operator fun invoke(chatId: Long, text: String) =
        repo.send(chatId, text)
}