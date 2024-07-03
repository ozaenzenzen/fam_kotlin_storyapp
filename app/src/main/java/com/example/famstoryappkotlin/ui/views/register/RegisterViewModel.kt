package com.example.famstoryappkotlin.ui.views.register

import androidx.lifecycle.ViewModel
import com.example.famstoryappkotlin.data.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)
}