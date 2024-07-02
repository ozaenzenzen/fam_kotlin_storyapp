package com.example.famstoryappkotlin.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famstoryappkotlin.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }
}