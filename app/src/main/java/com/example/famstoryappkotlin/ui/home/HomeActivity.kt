package com.example.famstoryappkotlin.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.StoryItem
import com.example.famstoryappkotlin.databinding.ActivityHomeBinding
import com.example.famstoryappkotlin.ui.addstory.AddStoryActivity
import com.example.famstoryappkotlin.ui.detailstory.DetailStoryActivity
import com.example.famstoryappkotlin.ui.main.MainActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var viewModel: HomeViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar("For Your Page")
        setupViewModel()

        lifecycleScope.launch {
            launch {
                pageLoadingHandler(true)
                viewModel.getAuthenticationToken().collect() { token ->
                    viewModel.getAllStory(token ?: "").collect { response ->
                        response.onSuccess { data ->
                            val emptyList: List<StoryItem?>? = emptyList()
                            pageLoadingHandler(false)
                            data.listStory?.let { setRecycleViewData(it) }
                        }
                        response.onFailure {
                            pageLoadingHandler(false)
                        }
                    }
                }
                pageLoadingHandler(false)
            }
        }

        binding.fabCreateStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
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
//            setDisplayHomeAsUpEnabled(true)
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

    private fun setRecycleViewData(listStoryData: List<StoryItem?>) {
        val listStoryAdapter = ListStoryAdapter()
        listStoryAdapter.submitList(listStoryData)
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = listStoryAdapter
            setHasFixedSize(true)
        }
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryItem) {
                goToDetailStory(story)
            }
        })
    }

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