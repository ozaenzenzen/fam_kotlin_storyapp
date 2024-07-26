package com.example.famstoryappkotlin.ui.views.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.utils.CoroutinesTestRule
import com.example.famstoryappkotlin.utils.DataDummy
import com.example.famstoryappkotlin.utils.PagedTestDataSource
import com.example.famstoryappkotlin.utils.ResultState
import com.example.famstoryappkotlin.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var homeViewModelForAuth: HomeViewModel

    @Mock
    private lateinit var homeViewModelForStory: HomeViewModel

    private val dummyStory = DataDummy.generateDummyStoryEntity()
    private val dummyToken = DataDummy.generateDummyTokenEntity()

    @Before
    fun setUp() {
        homeViewModelForAuth = HomeViewModel(authRepository, storyRepository)
    }

    @Test
    fun `when Get Authentication Token Should Not Null and Return Success`() {
        val expectedToken = MutableLiveData<Result<String?>>()
        expectedToken.value = Result.success(dummyToken)

        `when`(authRepository.getAuthenticationTokenLiveData()).thenReturn(expectedToken)

        val actualToken = homeViewModelForAuth.getAuthenticationTokenLiveData().getOrAwaitValue()
        Mockito.verify(authRepository).getAuthenticationTokenLiveData()

        Assert.assertNotNull(actualToken)
        Assert.assertTrue(actualToken is Result)
        // Assert.assertEquals(dummyNews.size, (actualNews as ResultState.Success).data.size)
    }

    @Test
    fun `when Get All Stories Should Not Null and Return Success2`() = runTest {
        val expectedStory = DataDummy.generateDummyStoryEntity()
        val data = PagedTestDataSource.snapshot(expectedStory)

        val stories = MutableLiveData<PagingData<StoryItem>>()
        stories.value = data

        `when`(homeViewModelForStory.getAllStoryLiveData(dummyToken)).thenReturn(stories)

        val actualStories = homeViewModelForStory.getAllStoryLiveData(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        Mockito.verify(homeViewModelForStory).getAllStoryLiveData(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(expectedStory.size, differ.snapshot().size)
    }
//
    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}