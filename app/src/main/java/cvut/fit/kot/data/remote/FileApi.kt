package cvut.fit.kot.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

interface FileApi {
    @Headers("Accept: image/*")
    @GET("users/{userId}/avatar")
    @Streaming
    suspend fun downloadAvatar(@Path("userId") userId: Long
    ): Response<ResponseBody>
}