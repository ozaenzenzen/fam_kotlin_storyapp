package com.example.famstoryappkotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.famgithubuser1.data.retrofit.ApiService
import com.example.famstoryappkotlin.data.paging.StoryPagingSource
import com.example.famstoryappkotlin.data.response.AddStoryResponseModel
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.data.response.GetAllStoryResponseModel
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.utils.ResultState
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
            val bearerToken = "Bearer $token"
            val response = apiService.addStory(
                bearerToken,
                photo,
                description,
                lat,
                long,
            )
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("addStory", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun getAllStory(token: String): Flow<Result<GetAllStoryResponseModel>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.getAllStory(bearerToken, size = 30)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("getAllStory", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun getAllStory2(token: String): Flow<Result<PagingData<StoryItem>>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5,
                ),
                pagingSourceFactory = {
                    StoryPagingSource(apiService = apiService, bearerToken)
                }
            ).flow.collect { data ->
                emit(Result.success(data))
            }

        } catch (e: Exception) {
            Log.e("getAllStory", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun getAllStoryLiveData(token: String): LiveData<PagingData<StoryItem>> = liveData {
        val bearerToken = "Bearer $token"
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService = apiService, bearerToken)
            }
        ).liveData.value
    }

    fun getAllStoryList(token: String): Flow<ResultState<PagingData<StoryItem>>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5,
                ),
                pagingSourceFactory = {
                    StoryPagingSource(apiService = apiService, bearerToken)
                }
            ).flow.collect { data ->
                emit(ResultState.Success(data))
            }

        } catch (e: Exception) {
            Log.e("getAllStory", e.message.toString())
            emit(ResultState.Error(e.message.toString()))
        }
    }


    fun getAllStoryWithLoc(token: String): Flow<Result<GetAllStoryResponseModel>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.getAllStory(bearerToken, size = 30, location = 1)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("getAllStory", e.message.toString())
            emit(Result.failure(e))
        }
    }

    fun detailStory(token: String, id: String): Flow<Result<DetailStoryResponseModel>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.detailStory(bearerToken, id)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("detailStory", e.message.toString())
            emit(Result.failure(e))
        }
    }
}