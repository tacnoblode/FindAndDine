package com.howellkeatingfonseca.findanddine.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.howellkeatingfonseca.findanddine.screens.SplashScreen

@Composable
fun AppNavigation(navController: NavController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController as androidx.navigation.NavHostController,
        startDestination = "splash_screen",
        modifier = modifier
    ) {
        // TODO: Add the rest of the screens to the navigation list as they're finished
        composable("splash_screen") {
            SplashScreen(
                modifier = Modifier
            )
        }
    }
}