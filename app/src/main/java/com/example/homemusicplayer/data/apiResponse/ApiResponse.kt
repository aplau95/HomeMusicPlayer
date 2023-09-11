package com.example.homemusicplayer.data.apiResponse

sealed class ApiResponse<out T> {
    object Loading : ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ) : ApiResponse<T>()

    data class Failure<out T>(
        val errorMessage: String,
        val code: Int
    ) : ApiResponse<T>()

}

