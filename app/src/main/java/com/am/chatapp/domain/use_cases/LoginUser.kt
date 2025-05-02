package com.am.chatapp.domain.use_cases

import com.am.chatapp.domain.repository.AuthRepository
import com.am.chatapp.domain.repository.AuthResult
import javax.inject.Inject

class LoginUser @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.login(email, password)
    }
}