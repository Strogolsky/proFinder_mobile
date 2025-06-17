package cvut.fit.kot.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import cvut.fit.kot.data.local.SessionDataStore
import cvut.fit.kot.util.JwtUtils
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDataStore: SessionDataStore
) {

    fun tokenFlow(): Flow<String?> = sessionDataStore.tokenFlow
    fun roleFlow():  Flow<String?> = sessionDataStore.roleFlow

    suspend fun save(token: String, role: String) =
        sessionDataStore.save(token, role)

    suspend fun saveToken(token: String) =
        sessionDataStore.saveToken(token)

    suspend fun clear() = sessionDataStore.clear()


    fun userIdFlow(): Flow<Long?> =
        sessionDataStore.tokenFlow.map { token ->
            token?.let(JwtUtils::userId)
        }

    suspend fun userId(): Long? =
        sessionDataStore.tokenFlow
            .firstOrNull()
            ?.let(JwtUtils::userId)
}
