package com.example.famgithubuser1.data.retrofit

import com.example.famstoryappkotlin.data.response.AddStoryResponseModel
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.data.response.GetAllStoryResponseModel
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import com.example.famstoryappkotlin.data.response.RegisterResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //@Headers("Authorization: Bearer ${BuildConfig.API_KEY}")
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Body name: String,
        @Body email: String,
        @Body password: String,
    ): RegisterResponseModel

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Body email: String,
        @Body password: String,
    ): LoginResponseModel

    @Multipart
    @POST("stories")
    @Headers("Content-Type: multipart/form-data")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part lat: RequestBody?,
        @Part long: RequestBody?,
    ): AddStoryResponseModel

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): GetAllStoryResponseModel

    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponseModel
}