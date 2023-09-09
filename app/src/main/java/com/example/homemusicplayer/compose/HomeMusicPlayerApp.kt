package com.example.homemusicplayer.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homemusicplayer.compose.home.Home
import com.example.homemusicplayer.compose.library.Library

@Composable
fun HomeMusicPlayerApp() {
    val navController = rememberNavController()
    HomeMusicPlayerNavHost(
        navController
    )
}

@Composable
fun HomeMusicPlayerNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home() }
    }
}