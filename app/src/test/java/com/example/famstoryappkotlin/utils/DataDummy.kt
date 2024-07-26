package com.example.famstoryappkotlin.utils

import com.example.famstoryappkotlin.data.response.StoryItem

object DataDummy {
    fun generateDummyStoryEntity(): List<StoryItem> {
        val storyList = ArrayList<StoryItem>()
        for (i in 0..10) {
            val story = StoryItem(
                id = "story-aK6FDM-OL9VbQVo9",
                name = "Dicoding",
                description = "Pengalaman belajar di Dicoding waktu itu sangat mengesankan. Materinya relevan karena Dicoding adalah  Google Authorized Training Partner. Semua modulnya telah diverifikasi langsung oleh Google. Apalagi, penyampaian materi juga luwes dalam bahasa Indonesia.",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687835610920_GBYv03RV.png",
                createdAt = "2023-06-27T03:13:30.924Z",
                lat = -6.9033625,
                lon = 107.6019579,
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyTokenEntity(): String {
        return "SAmpleToken"
    }
//    fun generateDummyNewsEntity(): List<NewsEntity> {
//        val newsList = ArrayList<NewsEntity>()
//        for (i in 0..10) {
//            val news = NewsEntity(
//                "Title $i",
//                "2022-02-02T22:22:22Z",
//                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
//                "https://www.dicoding.com/",
//            )
//            newsList.add(news)
//        }
//        return newsList
//    }
//
//    fun generateDummyNewsResponse(): NewsResponse {
//        val newsList = ArrayList<ArticlesItem>()
//        for (i in 1..10) {
//            val news = ArticlesItem(
//                "2022-02-22T22:22:22Z",
//                "author $i",
//                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
//                "description $i",
//                Source("name", "id"),
//                "Title $i",
//                "https://www.dicoding.com/",
//                "content $i",
//            )
//            newsList.add(news)
//        }
//        return NewsResponse(newsList.size, newsList, "Success")
//    }
}