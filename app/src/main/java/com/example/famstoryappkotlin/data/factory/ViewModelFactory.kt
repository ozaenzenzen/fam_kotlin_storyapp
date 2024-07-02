package com.example.famstoryappkotlin.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.ui.login.LoginViewModel
import com.example.famstoryappkotlin.ui.register.RegisterViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}