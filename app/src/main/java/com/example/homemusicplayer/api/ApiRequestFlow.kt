package com.example.homemusicplayer.api

import android.util.Log
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class ApiRequestFlow {

    companion object {

        fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> =
            flow<ApiResponse<T>> {
                emit(ApiResponse.Loading)

                withTimeoutOrNull(20000L) {
                    val response = call()

                    Log.e("YOOOO", response.message())

                    try {
                        if (response.isSuccessful) {
                            Log.e("YOOOO", response.message())
                            response.body()?.let { data ->
                                emit(ApiResponse.Success(data))
                            }
                        } else {
                            response.errorBody()?.let { error ->
                                error.close()
                                val parsedError: ErrorResponse =
                                    Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                                emit(ApiResponse.Failure(parsedError.message, parsedError.code))
                            }
                        }
                    } catch (e: Exception) {
                        emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
                    }
                } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
            }.flowOn(Dispatchers.IO)
    }
}