package com.am.chatapp.presentation.screens

sealed class Screen (val route: String){
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register: Screen("register")
    object Profile: Screen("profile")
}