package com.example.stockapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.stockapp.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "STOCK_PREFERENCE"
)
class DataStoreRepositoryImpl(context: Context) : DataStoreRepository {
    private val datastore = context.dataStore

    private suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    private suspend fun <T> putValue(key: Preferences.Key<T>, value: T) {
        datastore.edit { preferences ->
            preferences[key] = value
        }
    }
    override suspend fun getString(key: String, defaultValue: String): String {
        val dsKey = stringPreferencesKey(key)
        return getValue(dsKey, defaultValue).firstOrNull() ?: defaultValue
    }

    override suspend fun putString(key: String, value: String) {
        val dsKey = stringPreferencesKey(key)
        putValue(dsKey, value)
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        val dsKey = intPreferencesKey(key)
        return getValue(dsKey, defaultValue).firstOrNull() ?: defaultValue
    }

    override suspend fun putInt(key: String, value: Int) {
        val dsKey = intPreferencesKey(key)
        putValue(dsKey, value)
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val dsKey = booleanPreferencesKey(key)
        return getValue(dsKey, defaultValue).firstOrNull() ?: defaultValue
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val dsKey = booleanPreferencesKey(key)
        putValue(dsKey, value)
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        val dsKey = longPreferencesKey(key)
        return getValue(dsKey, defaultValue).firstOrNull() ?: defaultValue
    }

    override suspend fun putLong(key: String, value: Long) {
        val dsKey = longPreferencesKey(key)
        putValue(dsKey, value)
    }
}