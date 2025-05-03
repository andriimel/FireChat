package com.am.chatapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.chatapp.domain.repository.AuthResult
import com.am.chatapp.domain.use_cases.LoginUser
import com.am.chatapp.domain.use_cases.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUser: RegisterUser,
    private val loginUser: LoginUser   
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult: StateFlow<AuthResult> get() = _authResult

    private val _isLoggedIn = MutableStateFlow(false) // log in status
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun registerUserEmailPassword(email: String, password: String, confirmationPassword: String, nickname: String) {

        if (confirmationPassword != password) {
            _authResult.value = AuthResult.Error("Password do not match")
            return

        }
        if (nickname.isBlank()){
            _authResult.value = AuthResult.Error("Pls input username !!!")
            return

        }
        viewModelScope.launch {
            val result = registerUser(email, password, nickname)
            _authResult.value = result

        }
    }

    fun loginUserEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = loginUser(email, password)
            _authResult.value = result
            if (result is AuthResult.Success){
                _isLoggedIn.value = true
            }
        }
    }
    fun resetAuthResult() {
        _authResult.value = AuthResult.Idle
    }
}