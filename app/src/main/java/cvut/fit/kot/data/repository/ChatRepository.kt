package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.model.CreateChatRequest
import cvut.fit.kot.data.remote.ChatApi
import cvut.fit.kot.data.remote.ChatDataStore
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ChatApi,
    private val ws: ChatDataStore
) {
    suspend fun getChatsProfile(): Response<List<ChatResponse>> =
        api.getChatsProfile()

    suspend fun getHistory(chatId: Long): Response<List<ChatMessageResponse>> =
        api.getChatHistory(chatId)

    fun observe(chatId: Long) =
        ws.listen(chatId)
    fun send(chatId: Long, text: String) =
        ws.send(chatId, text)

    suspend fun create(request: CreateChatRequest): Response<ChatResponse> =
        api.create(request)
}