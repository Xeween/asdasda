package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hub_operator_prefs")

data class OperatorSettings(
    val hapticFeedback: Boolean = true,
    val compactDensity: Boolean = false,
    val showSystemTerminal: Boolean = true,
    val autoHashtagOnPost: Boolean = true,
    val lastSyncCheckpoint: String = "LOCAL // ACTIVE"
)

class SettingsDataStore(private val context: Context) {
    private object Keys {
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val COMPACT_DENSITY = booleanPreferencesKey("compact_density")
        val SHOW_SYSTEM_TERMINAL = booleanPreferencesKey("show_system_terminal")
        val AUTO_HASHTAG = booleanPreferencesKey("auto_hashtag")
        val LAST_CHECKPOINT = stringPreferencesKey("last_checkpoint")
    }

    val settingsFlow: Flow<OperatorSettings> = context.dataStore.data.map { prefs ->
        OperatorSettings(
            hapticFeedback = prefs[Keys.HAPTIC_FEEDBACK] ?: true,
            compactDensity = prefs[Keys.COMPACT_DENSITY] ?: false,
            showSystemTerminal = prefs[Keys.SHOW_SYSTEM_TERMINAL] ?: true,
            autoHashtagOnPost = prefs[Keys.AUTO_HASHTAG] ?: true,
            lastSyncCheckpoint = prefs[Keys.LAST_CHECKPOINT] ?: "LOCAL // ACTIVE"
        )
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { it[Keys.HAPTIC_FEEDBACK] = enabled }
    }

    suspend fun setCompactDensity(enabled: Boolean) {
        context.dataStore.edit { it[Keys.COMPACT_DENSITY] = enabled }
    }

    suspend fun setShowSystemTerminal(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_SYSTEM_TERMINAL] = enabled }
    }

    suspend fun setAutoHashtagOnPost(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_HASHTAG] = enabled }
    }

    suspend fun updateCheckpoint(label: String) {
        context.dataStore.edit { it[Keys.LAST_CHECKPOINT] = label }
    }
}
