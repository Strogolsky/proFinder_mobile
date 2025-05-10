package cvut.fit.kot.data.model

data class SignUpRequest(
    val email: String,
    val password: String,
    val role: String
)