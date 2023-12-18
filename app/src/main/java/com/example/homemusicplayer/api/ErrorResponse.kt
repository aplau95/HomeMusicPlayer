package com.example.homemusicplayer.api

/**
 * Data class for ErrorResponse from API
 */
data class ErrorResponse(
    val code: Int,
    val message: String
)