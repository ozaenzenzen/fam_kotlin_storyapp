package com.example.famstoryappkotlin.data.repository

import android.util.Log
import com.example.famgithubuser1.data.retrofit.ApiService
import com.example.famstoryappkotlin.data.response.AddStoryResponseModel
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.data.response.GetAllStoryResponseModel
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {
    suspend fun addStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        long: RequestBody? = null
    ): Flow<Result<AddStoryResponseModel>> = flow {
        try {
            val response = apiService.addStory(
                token,
                description,
                photo,
                lat,
                long,
            )
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun getAllStory(token: String): Flow<Result<GetAllStoryResponseModel>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.getAllStory(bearerToken, size = 30)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun detailStory(token: String, id: String): Flow<Result<DetailStoryResponseModel>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.detailStory(bearerToken, id)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }
}