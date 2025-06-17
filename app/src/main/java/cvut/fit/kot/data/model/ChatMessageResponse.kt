package cvut.fit.kot.data.model

data class ChatMessageResponse(
    val id: Long,

    val chatId: Long,

    val content: String,

    val senderId: Long,

    val createAt: String
)
