package com.example.famstoryappkotlin.data.repository

import android.util.Log
import com.example.famgithubuser1.data.retrofit.ApiService
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import com.example.famstoryappkotlin.data.response.RegisterResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(private val apiService: ApiService) {
    suspend fun login(email: String, password: String): Flow<Result<LoginResponseModel>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }

    suspend fun register(name: String, email: String, password: String): Flow<Result<RegisterResponseModel>> = flow {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("login", e.message.toString())
            emit(Result.failure(e))
        }
    }
}