package cvut.fit.kot.data.model

data class AuthRequest(
    val email: String,
    val password: String,
    val role: String
)