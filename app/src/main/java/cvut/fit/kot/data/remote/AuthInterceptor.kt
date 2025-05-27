package cvut.fit.kot.data.remote

import cvut.fit.kot.data.local.SessionDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDS: SessionDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.url.encodedPath.startsWith("/auth")) {
            return chain.proceed(original)
        }

        val token = runBlocking { tokenDS.tokenFlow.first() }
        val authorised = if (!token.isNullOrEmpty()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else original

        return chain.proceed(authorised)
    }
}
