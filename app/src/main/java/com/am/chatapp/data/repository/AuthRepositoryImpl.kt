package com.am.chatapp.data.repository

import com.am.chatapp.domain.repository.AuthRepository
import com.am.chatapp.domain.repository.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl (
    private val firebaseAuth: FirebaseAuth
) : AuthRepository{
    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            AuthResult.Success("User registered successfully")
        } catch (e: Exception) {
            AuthResult.Error("Registration failed: ${e.message}")
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success("User logged in successfully")
        } catch (e: Exception) {
            AuthResult.Error("Login failed: ${e.message}")
        }
    }
}