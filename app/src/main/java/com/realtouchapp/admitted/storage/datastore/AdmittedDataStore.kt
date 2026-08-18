package com.realtouchapp.admitted.storage.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.admittedDatastore: DataStore<Preferences> by preferencesDataStore(name = "AdmittedDatastore")

class AdmittedDataStore(private val context: Context) {
    companion object {
        val KEY_FREE_VERSION = booleanPreferencesKey("KEY_FREE_VERSION")
        val KEY_BASIC_INFO_NATIONALITY= "KEY_BASIC_INFO_NATIONALITY"
        val KEY_BASIC_INFO_LANGUAGE="KEY_BASIC_INFO_LANGUAGE"
        val KEY_BASIC_INFO_SCHOOL= "KEY_BASIC_INFO_SCHOOL"
        val KEY_BASIC_INFO_GRADE="KEY_BASIC_INFO_GRADE"
        val KEY_BASIC_INFO_GRADE9="KEY_BASIC_INFO_GRADE9"
        val KEY_BASIC_INFO_GRADE10="KEY_BASIC_INFO_GRADE10"
        val KEY_BASIC_INFO_GRADE11="KEY_BASIC_INFO_GRADE11"
        val KEY_BASIC_INFO_SCHOOL_SYSTEM="KEY_BASIC_INFO_SCHOOL_SYSTEM"
        val KEY_BASIC_INFO_GRADING_SCALE="KEY_BASIC_INFO_GRADING_SCALE"
        val KEY_BASIC_INFO_SCHEDULE="KEY_BASIC_INFO_SCHEDULE"
        val KEY_BASIC_INFO_INTEREST="KEY_BASIC_INFO_INTEREST"
        val KEY_BASIC_INFO_TARGET_SCHOOL="KEY_BASIC_INFO_TARGET_SCHOOL"
        val KEY_BASIC_INFO_TESTLIST="KEY_BASIC_INFO_TESTLIST"
        const val KEY_PENDING_GOOGLE_FIRST_NAME = "KEY_PENDING_GOOGLE_FIRST_NAME"
        const val KEY_PENDING_GOOGLE_LAST_NAME = "KEY_PENDING_GOOGLE_LAST_NAME"
    }

    fun isFreeVersion() = context.admittedDatastore.data.map { preferences ->
        preferences[KEY_FREE_VERSION] ?: false
    }

    suspend fun setFreeVersion(isFreeVersion: Boolean) {
        context.admittedDatastore.edit { preferences ->
            preferences[KEY_FREE_VERSION] = isFreeVersion
        }
    }
    suspend fun saveDataClass(key: String,value: Any){
        value.apply {
            try {
                val builder = GsonBuilder().setLenient()
                val gson = builder.create()
                saveValue(key,gson.toJson(this))
            } catch (exception: Exception) {
                Log.e("Any", "toJson: $exception")
            }
        }
    }

    suspend fun saveValue(keys: String,value:String) {
        context.admittedDatastore.edit { preferences ->
            preferences[stringPreferencesKey(keys)] = value
        }
    }

    fun  getValue(key:String) = context.admittedDatastore.data.map { preferences ->
        preferences[stringPreferencesKey(key)] ?: ""
    }

    suspend fun clearAll() {
        context.admittedDatastore.edit { preferences ->
            preferences.clear()
        }
    }
}