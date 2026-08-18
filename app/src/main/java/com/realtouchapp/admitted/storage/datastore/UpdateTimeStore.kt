package com.realtouchapp.admitted.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.realtouchapp.admitted.ext.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
    作者: RobertChou
    時間: 2023/2/14 下午 11:59
    信箱: didi31139@gmail.com
    說明:
*/
class UpdateTimeStore (private val context: Context) {
    companion object{
        private val Context.updateTimeStore: DataStore<Preferences> by preferencesDataStore(name = "updateTime")

        private val KEY_GETALL = stringPreferencesKey("getall")
        private val KEY_COURSE= stringPreferencesKey("course")
        private val KEY_INTELLECTUALCURIOSITY= stringPreferencesKey("intellectualCuriosity")
        private val KEY_COMMUNITYIMPACT= stringPreferencesKey("communityImpact")
        private val KEY_NOTICIATION= stringPreferencesKey("notificaiton")
        private val KEY_TARGETSCHOOL= stringPreferencesKey("targetschool")
        private val KEY_APPLICATIONSCHOOL= stringPreferencesKey("applicationschool")
        private val KEY_HONORS= stringPreferencesKey("honors")
        private val KEY_TEST= stringPreferencesKey("test")

    }

    suspend fun deleteAll() {
        context.dataStore
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_GETALL] = ""
            preferences[KEY_COURSE] = ""
            preferences[KEY_INTELLECTUALCURIOSITY] = ""
            preferences[KEY_COMMUNITYIMPACT] = ""
            preferences[KEY_NOTICIATION] = ""
            preferences[KEY_TARGETSCHOOL] = ""
            preferences[KEY_APPLICATIONSCHOOL] = ""
            preferences[KEY_HONORS] = ""
            preferences[KEY_TEST] = ""
            preferences.clear()
        }
    }

    val getGetAll: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_GETALL]
    }

    suspend fun saveGetAll(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_GETALL] = updateTime
        }
    }

    val getCourse: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_COURSE]
    }

    suspend fun saveCourse(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_COURSE] = updateTime
        }
    }

    val getIntellectualcuriosity: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_INTELLECTUALCURIOSITY]
    }

    suspend fun saveIntellectualcuriosity(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_INTELLECTUALCURIOSITY] = updateTime
        }
    }
    val getNotification: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_NOTICIATION]
    }

    suspend fun saveNotification(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_NOTICIATION] = updateTime
        }
    }

    val getCommunityimpact: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_COMMUNITYIMPACT]
    }

    suspend fun saveCommunityimpact(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_COMMUNITYIMPACT] = updateTime
        }
    }

    val getHonors: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_HONORS]
    }

    suspend fun saveHonors(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_HONORS] = updateTime
        }
    }

    val getTest: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_TEST]
    }

    suspend fun saveTest(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_TEST] = updateTime
        }
    }

    val getTargetSchool: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_TARGETSCHOOL]
    }

    suspend fun saveTargetSchhol(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_TARGETSCHOOL] = updateTime
        }
    }


    val getApplicationSchool: Flow<String?> = context.updateTimeStore.data.map { preferences ->
        preferences[KEY_APPLICATIONSCHOOL]
    }

    suspend fun saveApplicationSchool(updateTime: String) {
        context.updateTimeStore.edit { preferences ->
            preferences[KEY_APPLICATIONSCHOOL] = updateTime
        }
    }
}