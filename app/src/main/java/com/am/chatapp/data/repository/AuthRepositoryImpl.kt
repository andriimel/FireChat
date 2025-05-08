package com.am.chatapp.data.repository

import android.util.Log
import com.am.chatapp.domain.repository.AuthRepository
import com.am.chatapp.domain.repository.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import javax.annotation.meta.TypeQualifierNickname
import javax.inject.Inject

class AuthRepositoryImpl @Inject  constructor (
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository{
    override suspend fun register(email: String, password: String, nickname: String): AuthResult {
        return try {
            val result =  firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            val uid = result.user?.uid

            uid?.let {
                val userMap = mapOf(
                    "uid" to it,
                    "email" to email,
                    "nickname" to nickname
                )
                firestore.collection("users").document(it).set(userMap).await()
            }
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
    override suspend fun logout(): AuthResult {
        return try {
            firebaseAuth.signOut()
            AuthResult.Success("Logged out successfully")
        } catch (e: Exception) {
            AuthResult.Error("Logout failed: ${e.message}")
        }
    }


}