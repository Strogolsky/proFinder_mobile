package cvut.fit.kot.data.remote

import cvut.fit.kot.data.model.SignUpRequest
import cvut.fit.kot.data.model.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/signUp")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>
}