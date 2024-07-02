package com.example.famstoryappkotlin.ui.detailstory

import androidx.lifecycle.ViewModel
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class DetailStoryViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    suspend fun detailStory(token: String, id: String) = storyRepository.detailStory(token, id)

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }
}