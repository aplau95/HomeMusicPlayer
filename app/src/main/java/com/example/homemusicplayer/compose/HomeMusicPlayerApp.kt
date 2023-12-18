package com.example.homemusicplayer.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homemusicplayer.compose.home.Home

// Composable entry point of app
@Composable
fun HomeMusicPlayerApp() {
    val navController = rememberNavController()
    HomeMusicPlayerNavHost(
        navController
    )
}

// Responsible for handling navigation of disparate parts of app when we implement views that are not
// in the Home Tab layout
@Composable
fun HomeMusicPlayerNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home() }
    }
}