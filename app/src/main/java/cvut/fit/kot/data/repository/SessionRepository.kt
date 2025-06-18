package cvut.fit.kot.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import cvut.fit.kot.data.local.SessionDataSource
import cvut.fit.kot.util.JwtUtils
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDataSource: SessionDataSource
) {

    fun tokenFlow(): Flow<String?> = sessionDataSource.tokenFlow
    fun roleFlow():  Flow<String?> = sessionDataSource.roleFlow

    suspend fun save(token: String, role: String) =
        sessionDataSource.save(token, role)

    suspend fun saveToken(token: String) =
        sessionDataSource.saveToken(token)

    suspend fun clear() = sessionDataSource.clear()


    fun userIdFlow(): Flow<Long?> =
        sessionDataSource.tokenFlow.map { token ->
            token?.let(JwtUtils::userId)
        }

    suspend fun userId(): Long? =
        sessionDataSource.tokenFlow
            .firstOrNull()
            ?.let(JwtUtils::userId)
}
