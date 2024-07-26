package com.example.famstoryappkotlin.ui.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    suspend fun getAllStory(token: String) = storyRepository.getAllStory(token)

    suspend fun getAllStory2(token: String): Flow<Result<PagingData<StoryItem>>> =
        storyRepository.getAllStory2(token)

    fun getAllStoryLiveData(token: String): LiveData<PagingData<StoryItem>> =
        storyRepository.getAllStoryLiveData(token)

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }

    fun getAuthenticationTokenLiveData() = authRepository.getAuthenticationTokenLiveData()

    fun saveAuthenticationToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthenticationToken(token)
        }
    }
}