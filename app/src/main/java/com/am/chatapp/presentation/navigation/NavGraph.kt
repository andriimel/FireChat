package com.am.chatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.am.chatapp.presentation.auth.AuthState
import com.am.chatapp.presentation.auth.AuthViewModel
import com.am.chatapp.presentation.screens.LoginScreen
import com.am.chatapp.presentation.screens.ProfileScreen
import com.am.chatapp.presentation.screens.RegisterScreen
import com.am.chatapp.presentation.screens.SplashScreen
import com.am.chatapp.presentation.screens.Screen

@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {

    val authState = authViewModel.isLoggedIn.collectAsState().value
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateNext = {
                when (authState) {
                    is AuthState.Loading -> {
                        //
                    }
                    else -> {
                        if (authViewModel.isLoggedIn.value) {
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController,authViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, authViewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}