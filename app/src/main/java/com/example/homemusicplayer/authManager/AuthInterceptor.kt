package com.example.homemusicplayer.authManager

import com.example.homemusicplayer.persist.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * If appends the required token to the header for the HTTP request. If there is an unauthorized
 * error, OKHTTPClient will automatically send it to the AuthAuthenticator to do a token refresh
 */
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getJWTToken().first()
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}