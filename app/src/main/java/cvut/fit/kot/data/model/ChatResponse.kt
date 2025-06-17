package cvut.fit.kot.data.model

data class ChatResponse (
    val chatId: Long,

    val partnerId: Long,

    val partnerFirstName: String?
)