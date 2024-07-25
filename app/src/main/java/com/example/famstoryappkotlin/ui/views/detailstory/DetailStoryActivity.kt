package com.example.famstoryappkotlin.ui.views.detailstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.databinding.ActivityDetailStoryBinding
import com.example.famstoryappkotlin.ui.views.MapsActivity
import com.example.famstoryappkotlin.ui.views.addstory.AddStoryActivity
import com.example.famstoryappkotlin.ui.views.home.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    private lateinit var viewModel: DetailStoryViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository

    private var idStory: String? = null

    private var lat: Double? = null
    private var long: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        idStory = intent.extras?.get(EXTRA_DETAIL) as String
        setContentView(binding.root)

        setToolbar("Detail Story")
        setupViewModel()

        lifecycleScope.launch {
            launch {
                viewModel.getAuthenticationToken().collect() { token ->
                    token?.let {
                        viewModel.detailStory(it, idStory!!).collect { response ->
                            response.onSuccess { detailStoryData ->
                                detailStoryData.let {
                                    if (detailStoryData?.story?.lat != null && detailStoryData?.story?.lon != null) {
                                        lat = detailStoryData?.story?.lat.toString().toDouble()
                                        long = detailStoryData?.story?.lon.toString().toDouble()
                                    }
                                    binding.tvDetailName.text = it.story?.name
                                    binding.tvDetailDescription.text = it.story?.description
                                    Glide
                                        .with(this@DetailStoryActivity)
                                        .load(it.story?.photoUrl.toString())
                                        .placeholder(R.drawable.image_item2)
                                        .into(binding.ivDetailPhoto)
                                }
                            }
                            response.onFailure { }
                        }
                    }
                }
            }
        }

        binding.mapsButton.apply {
            if (lat != null && long != null) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            setOnClickListener {
                val intent = Intent(this@DetailStoryActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
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
        ).get(DetailStoryViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val TAG = "DetailUserActivity"
        const val EXTRA_DETAIL = "extra_detail"
    }
}