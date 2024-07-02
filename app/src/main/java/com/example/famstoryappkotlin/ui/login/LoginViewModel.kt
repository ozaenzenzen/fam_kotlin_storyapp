package com.example.famstoryappkotlin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famstoryappkotlin.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    suspend fun login(email: String, password: String) =
        authRepository.login(email, password)

    fun saveAuthenticationToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthenticationToken(token)
        }
    }
}