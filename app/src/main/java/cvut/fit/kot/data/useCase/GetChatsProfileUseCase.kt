package cvut.fit.kot.data.useCase

import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.repository.ChatRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetChatsProfileUseCase @Inject constructor(
    private val repository: ChatRepository
){
    suspend fun execute(): Result<List<ChatResponse>> = try {
        val response = repository.getChatsProfile()
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