package com.am.chatapp.domain.use_cases

import com.am.chatapp.domain.repository.AuthRepository
import com.am.chatapp.domain.repository.AuthResult
import javax.annotation.meta.TypeQualifierNickname
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password : String, nickname: String) : AuthResult{
        return repository.register(email,password, nickname)
    }
}