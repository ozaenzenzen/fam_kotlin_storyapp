package com.example.famstoryappkotlin.ui.views.maps

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.GetAllStoryResponseModel
import com.example.famstoryappkotlin.data.response.StoryItem
import kotlinx.coroutines.flow.Flow

class MapsViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }

    suspend fun getAllStoryWithLoc(token: String): Flow<Result<GetAllStoryResponseModel>> =
        storyRepository.getAllStoryWithLoc(token)
}