package com.am.chatapp.presentation.auth

sealed class AuthState {
    object Loading: AuthState()
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
}