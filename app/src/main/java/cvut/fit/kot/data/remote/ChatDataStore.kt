package cvut.fit.kot.data.remote

import okhttp3.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import com.google.gson.Gson
import cvut.fit.kot.data.local.SessionDataStore
import cvut.fit.kot.data.model.ChatMessageResponse
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

class ChatDataStore @Inject constructor(
    private val okHttp: OkHttpClient,
    @Named("BASE_WS_URL") private val baseWs: String,
    private val sessionDS: SessionDataStore,
    private val gson: Gson
) {

    private val sockets = ConcurrentHashMap<Long, WebSocket>()

    fun listen(chatId: Long): Flow<ChatMessageResponse> = callbackFlow {
        val jwt = runBlocking { sessionDS.tokenFlow.first() } ?: ""

        val request = Request.Builder()
            .url("$baseWs/chat/$chatId?token=$jwt")
            .build()

        val ws = okHttp.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                runCatching { gson.fromJson(text, ChatMessageResponse::class.java) }
                    .onSuccess { trySend(it).isSuccess }
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
            override fun onClosed (webSocket: WebSocket, code: Int, reason: String) {
                close()
            }
        })

        sockets[chatId] = ws

        awaitClose {
            sockets.remove(chatId)
            ws.close(1000, null)
        }
    }.buffer(Channel.UNLIMITED)

    fun send(chatId: Long, text: String): Boolean {
        val ws = sockets[chatId] ?: return false
        val payload = gson.toJson(mapOf("content" to text))
        return ws.send(payload)
    }
}
