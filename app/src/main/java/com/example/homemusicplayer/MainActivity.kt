package com.example.homemusicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apple.android.sdk.authentication.AuthenticationFactory
import com.apple.android.sdk.authentication.AuthenticationManager
import com.example.homemusicplayer.compose.HomeMusicPlayerApp
import com.example.homemusicplayer.service.TcpServerService
import com.example.homemusicplayer.ui.HomeMusicPlayerTheme
import com.example.homemusicplayer.viewModel.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<TokenViewModel>()

    private lateinit var mService: TcpServerService
    private var mBound: Boolean = false

    inner class Connection : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TcpServerService.TcpServerServiceBinder
            mService = binder.getService()
            mBound = true


            mService.trigger.observe(this@MainActivity) {
                println("Trigger is $it")
                if (it) {
                    // Use createIntentBuilder api to create the Intentbuilder which the 3rd party app can use to customize a few things
                    val params: HashMap<String, String> = HashMap<String, String>()
                    params["ct"] = "mytestCampaignToken"
                    params["at"] = "mytestAffiliateToken"
                    val intent =
                        authenticationManager.createIntentBuilder(viewModel.token.value)
                            .setHideStartScreen(false)
                            .setStartScreenMessage("Test this")
                            .setContextId("1100742453")
                            .setCustomParams(params)
                            .build()

                    startActivityForResult(intent, 3456)
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    private val connection by lazy { Connection() }

    override fun onStart() {
        super.onStart()

        Intent(applicationContext, TcpServerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }


    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    private val authenticationManager: AuthenticationManager =
        AuthenticationFactory.createAuthenticationManager(this)

    private fun startTCPServer() {
        val intent = Intent(applicationContext, TcpServerService::class.java)
        startService(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeMusicPlayerTheme {
                HomeMusicPlayerApp()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.saveToken(authenticationManager.handleTokenResult(data).musicUserToken)
    }

}