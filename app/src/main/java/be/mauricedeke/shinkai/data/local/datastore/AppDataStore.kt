package be.mauricedeke.shinkai.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import be.mauricedeke.shinkai.data.security.KeyVaultManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "shinkai_app")

@Singleton
class AppDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val keyVaultManager: KeyVaultManager
) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val BELT_VERSIONS_KEY = stringPreferencesKey("belt_versions")
        private val KATA_VERSIONS_KEY = stringPreferencesKey("kata_versions")
        private val LEXICON_VERSION_KEY = intPreferencesKey("lexicon_version")
    }

    // --- Token ---

    val accessToken: Flow<String?> = context.appDataStore.data.map { prefs ->
        prefs[TOKEN_KEY]?.let { runCatching { keyVaultManager.decrypt(it) }.getOrNull() }
    }

    suspend fun setAccessToken(token: String) {
        context.appDataStore.edit { it[TOKEN_KEY] = keyVaultManager.encrypt(token) }
    }

    suspend fun clearAccessToken() {
        context.appDataStore.edit { it.remove(TOKEN_KEY) }
    }

    // --- Belt versions ---

    suspend fun getBeltVersions(): Map<String, Int> =
        context.appDataStore.data.map { it[BELT_VERSIONS_KEY] }.first()
            ?.deserializeVersionMap() ?: emptyMap()

    suspend fun setBeltVersions(versions: Map<String, Int>) {
        context.appDataStore.edit { it[BELT_VERSIONS_KEY] = versions.serializeVersionMap() }
    }

    // --- Kata versions ---

    suspend fun getKataVersions(): Map<String, Int> =
        context.appDataStore.data.map { it[KATA_VERSIONS_KEY] }.first()
            ?.deserializeVersionMap() ?: emptyMap()

    suspend fun setKataVersions(versions: Map<String, Int>) {
        context.appDataStore.edit { it[KATA_VERSIONS_KEY] = versions.serializeVersionMap() }
    }

    // --- Lexicon version ---

    suspend fun getLexiconVersion(): Int =
        context.appDataStore.data.map { it[LEXICON_VERSION_KEY] ?: -1 }.first()

    suspend fun setLexiconVersion(version: Int) {
        context.appDataStore.edit { it[LEXICON_VERSION_KEY] = version }
    }

    // --- Helpers ---

    private fun Map<String, Int>.serializeVersionMap(): String {
        val obj = JSONObject()
        forEach { (k, v) -> obj.put(k, v) }
        return obj.toString()
    }

    private fun String.deserializeVersionMap(): Map<String, Int> = try {
        val obj = JSONObject(this)
        obj.keys().asSequence().associateWith { obj.getInt(it) }
    } catch (e: Exception) {
        emptyMap()
    }
}
