package com.am.chatapp.presentation

sealed class Screen (val route: String){
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register: Screen("register")
}