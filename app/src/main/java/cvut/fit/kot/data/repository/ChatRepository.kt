package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.model.CreateChatRequest
import cvut.fit.kot.data.remote.api.ChatApi
import cvut.fit.kot.data.remote.ChatDataSource
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ChatApi,
    private val ws: ChatDataSource
) {
    suspend fun getChatsProfile(): Result<List<ChatResponse>> = runCatching {
        val response = api.getChatsProfile()
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getHistory(chatId: Long): Result<List<ChatMessageResponse>> = runCatching {
        val response = api.getChatHistory(chatId)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    fun observe(chatId: Long) =
        ws.listen(chatId)

    fun send(chatId: Long, text: String) =
        ws.send(chatId, text)

    suspend fun create(request: CreateChatRequest): Response<ChatResponse> =
        api.create(request)
}