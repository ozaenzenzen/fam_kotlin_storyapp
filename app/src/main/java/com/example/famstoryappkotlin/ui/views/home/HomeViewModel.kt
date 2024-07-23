package com.example.famstoryappkotlin.ui.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.StoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    suspend fun getAllStory(token: String) = storyRepository.getAllStory(token)

    fun getAllStory2(token: String): Flow<Result<PagingData<StoryItem>>> =
        storyRepository.getAllStory2(token)

    fun getAllStory3(token: String): LiveData<PagingData<StoryItem>> =
        storyRepository.getAllStory3(token).cachedIn(viewModelScope).asLiveData()

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }

    fun saveAuthenticationToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthenticationToken(token)
        }
    }
}