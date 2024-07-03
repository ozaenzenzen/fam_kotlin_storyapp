package com.example.famstoryappkotlin.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.ui.views.addstory.AddStoryViewModel
import com.example.famstoryappkotlin.ui.views.detailstory.DetailStoryViewModel
import com.example.famstoryappkotlin.ui.views.home.HomeViewModel
import com.example.famstoryappkotlin.ui.views.login.LoginViewModel
import com.example.famstoryappkotlin.ui.views.main.MainViewModel
import com.example.famstoryappkotlin.ui.views.register.RegisterViewModel

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
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(authRepository, storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(authRepository, storyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(authRepository, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}