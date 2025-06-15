package cvut.fit.kot.data.remote

import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.model.CreateChatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @GET("chat/profile")
    suspend fun getChatsProfile(): Response<List<ChatResponse>>

    @GET("chat/{chatId}/history")
    suspend fun getChatHistory(@Path("chatId") chatId: Long): Response<List<ChatMessageResponse>>

    @POST("chat")
    suspend fun create(@Body request: CreateChatRequest): Response<ChatResponse>
}