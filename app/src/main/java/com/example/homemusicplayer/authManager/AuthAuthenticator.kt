package com.example.homemusicplayer.authManager

import android.util.Log
import com.example.homemusicplayer.BuildConfig
import com.example.homemusicplayer.persist.TokenManager
import io.github.nefilim.kjwt.JWSES256Algorithm
import io.github.nefilim.kjwt.JWT
import io.github.nefilim.kjwt.JWTKeyID
import io.github.nefilim.kjwt.sign
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.IOException
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.Base64
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        return runBlocking {
            val jwtResult = getNewToken()
            var newToken = ""

            val privateKey = getPrivateKey()

            jwtResult.sign(privateKey).fold(
                ifLeft = { tokenManager.deleteToken() },
                ifRight = {
                    newToken = it.rendered
                    tokenManager.saveToken(newToken)
                    Log.e("TOKEN", it.rendered)
                }
            )

            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
    }

    @Throws(IOException::class)
    fun getPrivateKey(): ECPrivateKey {
        val BC = BouncyCastleProvider()

        val text = BuildConfig.privateKey

        val bytes = Base64.getDecoder().decode(text)
        
        val spec = PKCS8EncodedKeySpec(bytes)
        val factory = KeyFactory.getInstance("EC", BC)

        return factory.generatePrivate(spec) as ECPrivateKey
    }

    private fun getNewToken(): JWT<JWSES256Algorithm> {

        val teamId = BuildConfig.teamId
        val keyId = BuildConfig.keyId

        val jwt = JWT.es256(JWTKeyID(keyId)) {
            claim("iss", teamId)
            issuedAt(Instant.now())
            expiresAt(Instant.now().plusSeconds((60 * 60 * 24).toLong()))
        }

        return jwt
    }
}