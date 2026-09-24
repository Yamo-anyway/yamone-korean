package com.yamone.korean.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences",
)

data class LanguagePreferenceState(
    val isLoaded: Boolean = false,
    val languageCode: String? = null,
)

class UserPreferencesRepository(
    private val context: Context,
) {
    val languagePreference: Flow<LanguagePreferenceState> =
        context.userPreferencesDataStore.data.map { preferences ->
            val storedCode = preferences[EXPLANATION_LANGUAGE_KEY]
            LanguagePreferenceState(
                isLoaded = true,
                languageCode = storedCode?.takeIf { it in SUPPORTED_LANGUAGE_CODES },
            )
        }

    suspend fun setExplanationLanguage(languageCode: String) {
        require(languageCode in SUPPORTED_LANGUAGE_CODES) {
            "Unsupported explanation language: $languageCode"
        }

        context.userPreferencesDataStore.edit { preferences ->
            preferences[EXPLANATION_LANGUAGE_KEY] = languageCode
        }
    }

    companion object {
        val SUPPORTED_LANGUAGE_CODES = setOf(
            "en",
            "es",
            "fr",
            "vi",
            "th",
            "id",
        )

        private val EXPLANATION_LANGUAGE_KEY = stringPreferencesKey(
            "explanation_language",
        )
    }
}
