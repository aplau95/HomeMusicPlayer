package com.example.homemusicplayer.api

import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

/**
 * Singleton class that wraps API calls and parses to have response sent down
 */
class ApiRequestFlow {

    companion object {

        // How many ms we want to wait before we let the flow observer know there is likely a
        // network error
        const val TIMEOUT = 20000L

        fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> =
            flow<ApiResponse<T>> {

                // The the observer know we are loading
                emit(ApiResponse.Loading)

                withTimeoutOrNull(TIMEOUT) {

                    // Trigger the api request as delineated by the caller
                    val response = call()

                    try {

                        // If successful response, pass the data downstream
                        if (response.isSuccessful) {
                            response.body()?.let { data ->
                                emit(ApiResponse.Success(data))
                            }
                        } else {
                            response.errorBody()?.let { error ->
                                error.close()
                                val parsedError: ErrorResponse =
                                    Gson().fromJson(error.charStream(), ErrorResponse::class.java)

                                // If error response, pass the data downstream
                                emit(ApiResponse.Failure(parsedError.message, parsedError.code))
                            }
                        }
                    } catch (e: Exception) {
                        // If timeout, pass the error downstream
                        emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
                    }
                } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
            }.flowOn(Dispatchers.IO)
    }
}