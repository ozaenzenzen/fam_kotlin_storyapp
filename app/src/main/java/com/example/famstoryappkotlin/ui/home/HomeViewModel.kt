package com.example.famstoryappkotlin.ui.home

import androidx.lifecycle.ViewModel
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    suspend fun getAllStory(token: String) = storyRepository.getAllStory(token)

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }
}