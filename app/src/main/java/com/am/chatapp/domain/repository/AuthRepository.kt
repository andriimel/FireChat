package com.am.chatapp.domain.repository

import android.os.Message

sealed class AuthResult {
    object Idle: AuthResult()
    object Loading : AuthResult()
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String?) : AuthResult()
}
interface AuthRepository {
    suspend fun register(email: String, password : String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
}