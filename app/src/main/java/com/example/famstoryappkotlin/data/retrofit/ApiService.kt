package com.example.famgithubuser1.data.retrofit

import com.example.famstoryappkotlin.data.response.AddStoryResponseModel
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.data.response.GetAllStoryResponseModel
import com.example.famstoryappkotlin.data.response.LoginResponseModel
import com.example.famstoryappkotlin.data.response.RegisterResponseModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //    @FormUrlEncoded
//    @Headers("Authorization: Bearer ${BuildConfig.API_KEY}")
    @POST("register")
    fun register(
        @Body name: String,
        @Body email: String,
        @Body password: String,
    ): Call<RegisterResponseModel>

    @POST("login")
    fun login(
        @Body email: String,
        @Body password: String,
    ): Call<LoginResponseModel>

    @Multipart
    @POST("stories")
    @Headers("Content-Type: multipart/form-data")
    fun addStory(
        @Header("Authorization") token: String,
        @Part description: String,
        @Part photo: MultipartBody.Part,
        @Part lat: String?,
        @Part long: String?,
    ): Call<AddStoryResponseModel>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Call<GetAllStoryResponseModel>

    @GET("stories/{id}")
    fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponseModel>
}