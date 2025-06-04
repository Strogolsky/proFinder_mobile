package cvut.fit.kot.data.model

data class ClientResponse(
    val id: Long,

    val email: String,

    val firstName: String,

    val lastName: String,

    val password: String,

    val phoneNumber: String,

    val location: LocationResponse,

    val createdAt: String,

    val avatarUrl: String?
)
