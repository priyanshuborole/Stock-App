package com.example.stockapp.domain.repository

interface DataStoreRepository {
    suspend fun getString(key: String, defaultValue: String): String

    suspend fun putString(key: String, value: String)

    suspend fun getInt(key: String, defaultValue: Int): Int

    suspend fun putInt(key: String, value: Int)

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean

    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun getLong(key: String, defaultValue: Long): Long

    suspend fun putLong(key: String, value: Long)
}