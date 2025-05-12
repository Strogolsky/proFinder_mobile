package cvut.fit.kot.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import cvut.fit.kot.data.local.SessionDataStore

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDataStore: SessionDataStore
) {

    fun tokenFlow(): Flow<String?> = sessionDataStore.tokenFlow

    fun roleFlow(): Flow<String?> = sessionDataStore.roleFlow

    suspend fun save(token: String, role: String) {
        sessionDataStore.save(token, role)
    }

    suspend fun clear() {
        sessionDataStore.clear()
    }
}