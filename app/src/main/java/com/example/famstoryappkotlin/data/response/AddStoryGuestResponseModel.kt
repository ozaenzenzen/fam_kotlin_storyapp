package com.example.famstoryappkotlin.data.response

import com.google.gson.annotations.SerializedName

 class AddStoryGuestResponseModel(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
