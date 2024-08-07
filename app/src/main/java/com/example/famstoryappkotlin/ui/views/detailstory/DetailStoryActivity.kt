package com.example.famstoryappkotlin.ui.views.detailstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.databinding.ActivityDetailStoryBinding
import com.example.famstoryappkotlin.ui.views.maps.MapsActivity
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    private lateinit var viewModel: DetailStoryViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository

    private var idStory: String? = null

    private lateinit var detailStoryDatas: DetailStoryResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        idStory = intent.extras?.get(EXTRA_DETAIL) as String
        setContentView(binding.root)

        setToolbar("Detail Story")
        setupViewModel()

        lifecycleScope.launch {
            launch {
                viewModel.getAuthenticationToken().collect { token ->
                    token?.let {
                        viewModel.detailStory(it, idStory!!).collect { response ->
                            response.onSuccess { detailStoryData ->
                                detailStoryData.let { it ->
                                    detailStoryDatas = it
                                    checkIsAnyLocationValue(it)
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
            setOnClickListener {
                Intent(this@DetailStoryActivity, MapsActivity::class.java).apply{
                    putExtra("dataDetail", detailStoryDatas)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    private fun checkIsAnyLocationValue(detailStoryData: DetailStoryResponseModel) {
        var lat: Double? = null
        var long: Double? = null
        Log.d("location", "data lat : ${detailStoryData?.story?.lat}")
        Log.d("location", "data lon : ${detailStoryData?.story?.lon}")
        if (detailStoryData?.story?.lat != null && detailStoryData?.story?.lon != null) {
            binding.mapsButton.visibility = View.VISIBLE
        } else {
            binding.mapsButton.visibility = View.GONE
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