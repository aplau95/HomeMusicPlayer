package com.example.homemusicplayer

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.apple.android.sdk.authentication.AuthenticationFactory
import com.apple.android.sdk.authentication.AuthenticationManager
import com.example.homemusicplayer.databinding.ActivityMainBinding
import com.example.homemusicplayer.service.TcpServerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var mService: TcpServerService
    private var mBound: Boolean = false


    inner class Connection: ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TcpServerService.TcpServerServiceBinder
            mService = binder.getService()
            mBound = true


            mService.trigger.observe(this@MainActivity) {
                println("Trigger is $it")
                if(it) {
                    // Use createIntentBuilder api to create the Intentbuilder which the 3rd party app can use to customize a few things
                    val params: HashMap<String, String> = HashMap<String, String>()
                    params["ct"] = "mytestCampaignToken"
                    params["at"] = "mytestAffiliateToken"
                    val intent = authenticationManager.createIntentBuilder(getString(R.string.developer_token))
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

        Intent(applicationContext, TcpServerService::class.java).also {intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }


    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    private val authenticationManager: AuthenticationManager = AuthenticationFactory.createAuthenticationManager(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val intent = Intent(applicationContext, TcpServerService::class.java)
        startService(intent)

        viewModel.trigger.observe(this) {
            println("Trigger is $it")
            if(it) {
                // Use createIntentBuilder api to create the Intentbuilder which the 3rd party app can use to customize a few things
                val params: HashMap<String, String> = HashMap<String, String>()
                params["ct"] = "mytestCampaignToken"
                params["at"] = "mytestAffiliateToken"
                val intent = authenticationManager.createIntentBuilder(getString(R.string.developer_token))
                    .setHideStartScreen(false)
                    .setStartScreenMessage("Test this")
                    .setContextId("1100742453")
                    .setCustomParams(params)
                    .build()


                startActivityForResult(intent, 3456)
            }

        }








    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("REQUEST CODE $requestCode")
        println("result from this ${authenticationManager.handleTokenResult(data).musicUserToken}")
    }


    private fun replaceMainFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.main_container, fragment, tag)
//        if (addToBackStack) {
//            fragmentTransaction.addToBackStack(null)
//        }
//        fragmentTransaction.commit()
    }



//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}