package com.example.famstoryappkotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.famgithubuser1.data.retrofit.ApiService
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import com.example.famstoryappkotlin.data.response.RegisterResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val apiService: ApiService,
    private val userDataPreferences: UserDataPreferences,
) {
    suspend fun login(email: String, password: String): Flow<Result<LoginResponseModel>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponseModel>> = flow {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("register", e.message.toString())
            emit(Result.failure(e))
        }
    }

    suspend fun saveAuthenticationToken(token: String) {
        userDataPreferences.saveAuthenticationToken(token)
    }

    fun getAuthenticationToken(): Flow<String?> {
        return userDataPreferences.getAuthenticationToken()
    }

    fun getAuthenticationTokenLiveData(): LiveData<Result<String?>> = liveData {
        try {
            val data = userDataPreferences.getAuthenticationToken().asLiveData().value
            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}