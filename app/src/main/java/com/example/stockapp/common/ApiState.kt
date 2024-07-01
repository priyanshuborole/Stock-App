package com.example.stockapp.common

sealed class ApiState<T>(val data: T? = null, val message: String? = null, val showLoading: Boolean = false) {
    class Success<T>(data: T) : ApiState<T>(data)
    class Error<T>(message: String, data: T? = null) : ApiState<T>(data, message)
    class Loading<T>(data: T? = null, showLoading: Boolean = true) : ApiState<T>(data = data,showLoading = showLoading)
}