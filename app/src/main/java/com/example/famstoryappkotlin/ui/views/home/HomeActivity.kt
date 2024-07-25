package com.example.famstoryappkotlin.ui.views.home

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.databinding.ActivityHomeBinding
import com.example.famstoryappkotlin.ui.views.addstory.AddStoryActivity
import com.example.famstoryappkotlin.ui.views.detailstory.DetailStoryActivity
import com.example.famstoryappkotlin.ui.views.main.MainActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var viewModel: HomeViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var listStoryAdapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar("For Your Page")
        setupViewModel()

        binding.swipeRefresh.setOnRefreshListener {
            pageLoadingHandler(true)
            lifecycleHandler()
            binding.swipeRefresh.isRefreshing = false
        }

        setRecycleViewData()
        lifecycleHandler()

        binding.fabCreateStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun lifecycleHandler() {
        lifecycleScope.launch {
            launch {
                pageLoadingHandler(true)
                viewModel.getAuthenticationToken().collect() { token ->
                    viewModel.getAllStory2(token ?: "").collect { response ->
                        response.onSuccess { data ->
                            val emptyList: List<StoryItem?>? = emptyList()
                            pageLoadingHandler(false)
                            updateRecyclerViewData(data)
                        }
                        response.onFailure {
                            pageLoadingHandler(false)
                        }
                    }
                }
                pageLoadingHandler(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleHandler()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                viewModel.saveAuthenticationToken("")
                Intent(this@HomeActivity, MainActivity::class.java).also { intent ->
                    startActivity(intent)
                    finish()
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            // setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun setupViewModel() {
        var pref = UserDataPreferences.getInstance(application.dataStore)

        var apiService = ApiConfig.getApiService()

        authRepository = AuthRepository(apiService, pref)
        storyRepository = StoryRepository(apiService)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(authRepository, storyRepository)
        ).get(HomeViewModel::class.java)
    }

    private fun setRecycleViewData() {
        listStoryAdapter = ListStoryAdapter()
//        listStoryAdapter.submitData(lifecycle, listStoryData)
        listStoryAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listStoryAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                // List empty or error
                binding?.apply {
//                    tvNotFoundError.animateVisibility(true)
//                    ivNotFoundError.animateVisibility(true)
//                    rvStories.animateVisibility(false)
                }
            } else {
                // List not empty
                binding?.apply {
//                    tvNotFoundError.animateVisibility(false)
//                    ivNotFoundError.animateVisibility(false)
//                    rvStories.animateVisibility(true)
                }
            }
            binding?.swipeRefresh?.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = binding.rvStories!!
            recyclerView.apply {
                adapter = listStoryAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listStoryAdapter.retry()
                    }
                )
                layoutManager = LinearLayoutManager(this@HomeActivity)
            }
//            binding.rvStories.apply {
//                layoutManager = LinearLayoutManager(this@HomeActivity)
//                adapter = listStoryAdapter.withLoadStateFooter(
//                    footer = LoadingStateAdapter {
//                        listStoryAdapter.retry()
//                    }
//                )
//                // setHasFixedSize(true)
//            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryItem) {
                goToDetailStory(story)
            }
        })
    }

    private fun updateRecyclerViewData(listStoryData: PagingData<StoryItem>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        listStoryAdapter.submitData(lifecycle, listStoryData)
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

//    private fun setRecycleViewData(listStoryData: List<StoryItem?>) {
//        val listStoryAdapter = ListStoryAdapter()
//        listStoryAdapter.submitList(listStoryData)
//        binding.rvStories.apply {
//            layoutManager = LinearLayoutManager(this@HomeActivity)
//            adapter = listStoryAdapter
//            setHasFixedSize(true)
//        }
//        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
//            override fun onItemClicked(story: StoryItem) {
//                goToDetailStory(story)
//            }
//        })
//    }

    private fun goToDetailStory(story: StoryItem?) {
        Intent(this@HomeActivity, DetailStoryActivity::class.java).apply {
            putExtra(DetailStoryActivity.EXTRA_DETAIL, story?.id)
        }.also {
            startActivity(it)
        }
    }

    private fun pageLoadingHandler(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                viewLoading.apply {
                    ObjectAnimator
                        .ofFloat(this, View.ALPHA, if (true) 1f else 0f)
                        .setDuration(400)
                        .start()
                }
            } else {
                viewLoading.apply {
                    ObjectAnimator
                        .ofFloat(this, View.ALPHA, if (false) 1f else 0f)
                        .setDuration(400)
                        .start()
                }
            }
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}