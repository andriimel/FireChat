package com.am.chatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.am.chatapp.chat.screen.ChatScreen
import com.am.chatapp.chat.viewmodel.ChatViewModel
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
                            navController.navigate(Screen.GroupChat.route) {
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

//        composable(Screen.Profile.route) {
//            ProfileScreen(navController = navController)
//        }

        composable (Screen.GroupChat.route){
            val chatViewModel: ChatViewModel = hiltViewModel()
            ChatScreen(
                navController = navController,
                viewModel = chatViewModel,
                authViewModel = authViewModel,
                onLogoutClick = {
                    authViewModel.logOutUser()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.GroupChat.route) { inclusive = true }
                    }
                }
            )
        }
    }
}