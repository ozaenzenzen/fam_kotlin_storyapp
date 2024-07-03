package com.example.famstoryappkotlin.ui.views.addstory

import androidx.lifecycle.ViewModel
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    suspend fun addStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        long: RequestBody? = null
    ) = storyRepository.addStory(
        token,
        photo,
        description,
        lat,
        long,
    )

    fun getAuthenticationToken(): Flow<String?> {
        return authRepository.getAuthenticationToken()
    }
}