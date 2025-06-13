package cvut.fit.kot.data.local

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataStore @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    private val prefs = ctx.sessionDataStore

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("access_token")
        private val KEY_ROLE  = stringPreferencesKey("user_role")
    }

    val tokenFlow: Flow<String?> = prefs.data.map { it[KEY_TOKEN] }
    val roleFlow:  Flow<String?> = prefs.data.map { it[KEY_ROLE] }

    suspend fun save(token: String, role: String) {
        prefs.edit { settings ->
            settings[KEY_TOKEN] = token
            settings[KEY_ROLE]  = role
        }
    }

    suspend fun saveToken(token: String) {
        prefs.edit { settings ->
            settings[KEY_TOKEN] = token
        }
    }

    suspend fun clear() {
        prefs.edit { it.clear() }
    }
}

private val Context.sessionDataStore by preferencesDataStore(
    name = "session_prefs",
    produceMigrations = { ctx ->
        listOf(SharedPreferencesMigration(ctx, "legacy"))
    },
    corruptionHandler = null
)
