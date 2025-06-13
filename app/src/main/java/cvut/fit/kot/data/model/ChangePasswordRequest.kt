package cvut.fit.kot.data.model

data class ChangePasswordRequest(
    val oldPassword: String,

    val newPassword: String,

    val confirmPassword: String
)