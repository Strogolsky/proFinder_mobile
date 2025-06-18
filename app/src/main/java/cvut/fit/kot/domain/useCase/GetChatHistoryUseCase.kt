package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.repository.ChatRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun invoke(chatId: Long): Result<List<ChatMessageResponse>> = try {
        val response = repository.getHistory(chatId)
        if (response.isSuccessful) {
            response.body()?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("Empty body"))
        } else {
            Result.failure(HttpException(response))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}