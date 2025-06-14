package cvut.fit.kot.data.model

data class ResetPasswordRequest (
    val email: String,

    val newPassword: String,

    val confirmPassword: String,

    val verificationCode: String
)