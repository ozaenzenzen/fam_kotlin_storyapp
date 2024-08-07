package com.example.famstoryappkotlin.data.response

import com.google.gson.annotations.SerializedName

 class GetAllStoryResponseModel(

	@field:SerializedName("listStory")
	 //val listStory: List<StoryItem?>? = null,
	val listStory: List<StoryItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

 class StoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)
