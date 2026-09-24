package com.yamone.korean.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.learningProgressDataStore by preferencesDataStore(name = "learning_progress")

data class LearningProgressState(
    val isLoaded: Boolean = false,
    val currentStageRoute: String? = null,
    val currentLessonId: String? = null,
    val completedLessonIds: Set<String> = emptySet(),
    val reviewLessonIds: Set<String> = emptySet(),
)

class LearningProgressRepository(private val context: Context) {
    val progress: Flow<LearningProgressState> =
        context.learningProgressDataStore.data.map { preferences ->
            LearningProgressState(
                isLoaded = true,
                currentStageRoute = preferences[CURRENT_STAGE_ROUTE_KEY],
                currentLessonId = preferences[CURRENT_LESSON_ID_KEY],
                completedLessonIds = preferences[COMPLETED_LESSON_IDS_KEY].orEmpty(),
                reviewLessonIds = preferences[REVIEW_LESSON_IDS_KEY].orEmpty(),
            )
        }

    suspend fun setCurrentPosition(stageRoute: String, lessonId: String? = null) {
        context.learningProgressDataStore.edit { preferences ->
            preferences[CURRENT_STAGE_ROUTE_KEY] = stageRoute
            if (lessonId == null) preferences.remove(CURRENT_LESSON_ID_KEY)
            else preferences[CURRENT_LESSON_ID_KEY] = lessonId
        }
    }

    suspend fun markLessonCompleted(lessonId: String) {
        context.learningProgressDataStore.edit { preferences ->
            preferences[COMPLETED_LESSON_IDS_KEY] =
                preferences[COMPLETED_LESSON_IDS_KEY].orEmpty() + lessonId
            preferences[REVIEW_LESSON_IDS_KEY] =
                preferences[REVIEW_LESSON_IDS_KEY].orEmpty() - lessonId
        }
    }

    suspend fun markLessonForReview(lessonId: String) {
        context.learningProgressDataStore.edit { preferences ->
            preferences[REVIEW_LESSON_IDS_KEY] =
                preferences[REVIEW_LESSON_IDS_KEY].orEmpty() + lessonId
        }
    }

    suspend fun markReviewResolved(lessonId: String) {
        context.learningProgressDataStore.edit { preferences ->
            preferences[REVIEW_LESSON_IDS_KEY] =
                preferences[REVIEW_LESSON_IDS_KEY].orEmpty() - lessonId
        }
    }

    companion object {
        private val CURRENT_STAGE_ROUTE_KEY = stringPreferencesKey("current_stage_route")
        private val CURRENT_LESSON_ID_KEY = stringPreferencesKey("current_lesson_id")
        private val COMPLETED_LESSON_IDS_KEY = stringSetPreferencesKey("completed_lesson_ids")
        private val REVIEW_LESSON_IDS_KEY = stringSetPreferencesKey("review_lesson_ids")
    }
}
