package com.example.famstoryappkotlin.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.famstoryappkotlin.data.response.StoryItem
import kotlinx.coroutines.flow.Flow

class PagedTestDataSource :
    PagingSource<Int, Flow<List<StoryItem>>>() {

    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<StoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}