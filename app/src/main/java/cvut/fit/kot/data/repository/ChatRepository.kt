package cvut.fit.kot.data.repository

import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.remote.ChatApi
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ChatApi
) {
    suspend fun getChatsProfile(): Response<List<ChatResponse>> =
        api.getChatsProfile()
}