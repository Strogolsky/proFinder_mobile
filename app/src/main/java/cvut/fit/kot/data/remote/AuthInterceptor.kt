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
        val t = runBlocking { tokenDS.tokenFlow.first() }
        val req = if (t != null)
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $t")
                .build()
        else chain.request()
        return chain.proceed(req)
    }
}