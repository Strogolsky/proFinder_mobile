package cvut.fit.kot.data.model

data class ClientResponse(
    val id: Long,

    val email: String,

    val firstName: String,

    val lastName: String,

    val phoneNumber: String,

    val location: LocationDto,

    val createdAt: String,

    val avatarUrl: String?
)
