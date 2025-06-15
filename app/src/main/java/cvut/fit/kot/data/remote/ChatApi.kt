package cvut.fit.kot.data.remote

import cvut.fit.kot.data.model.ChatResponse
import retrofit2.Response
import retrofit2.http.GET

interface ChatApi {
    @GET("chat/profile")
    suspend fun getChatsProfile(): Response<List<ChatResponse>>
}