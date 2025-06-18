package cvut.fit.kot.data.remote

import cvut.fit.kot.data.local.SessionDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDS: SessionDataSource
) : Interceptor {

    private val guestPaths = setOf(
        "auth/signUp",
        "auth/signIn"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path    = request.url.encodedPath

        if (path in guestPaths) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenDS.tokenFlow.first() }

        val authorisedRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authorisedRequest)
    }
}
