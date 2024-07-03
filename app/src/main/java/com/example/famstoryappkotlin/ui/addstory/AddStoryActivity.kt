package com.example.famstoryappkotlin.ui.addstory

import android.Manifest
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.AddStoryResponseModel
import com.example.famstoryappkotlin.databinding.ActivityAddStoryBinding
import com.example.famstoryappkotlin.ui.detailstory.DetailStoryViewModel
import com.example.famstoryappkotlin.utils.getImageUri
import com.example.famstoryappkotlin.utils.reduceFileImage
import com.example.famstoryappkotlin.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private lateinit var viewModel: AddStoryViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

//    private val launcherIntentCameraX = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == CAMERAX_RESULT) {
//            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
//            showImage()
//        }
//    }

    private fun allPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        setToolbar("Create Story")
        setupViewModel()

        setupButton()
    }

    private fun setupButton() {
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
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
        ).get(AddStoryViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo picker", "No Media Selected")
            }
        }

    val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                currentImageUri = uris[0]
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        // pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture())
        { isSuccess ->
            if (isSuccess) {
                showImage()
            }
        }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->

            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            // val description = "Ini adalah deskripsi gambar"
            val description = binding.etDescription2.text.toString().trim()

            pageLoadingHandler(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                launch {
                    try {
                        viewModel.getAuthenticationToken().collect { token ->
                            token.let {
                                viewModel.addStory(
                                    it ?: "",
                                    multipartBody,
                                    requestBody,
                                    null,
                                    null,
                                )
                                    .collect { response ->
                                        response.onSuccess { data ->
                                            data.let {
                                                if (it?.error == false) {
                                                    pageLoadingHandler(false)
                                                    showAlertDialog(
                                                        "Berhasil",
                                                        "Add story berhasil",
                                                        "Kembali",
                                                    ) {
                                                        finish()
                                                    }
                                                } else {
                                                    pageLoadingHandler(false)
                                                    showAlertDialog(
                                                        "Gagal",
                                                        "Add story gagal ${it.message}",
                                                        "Kembali",
                                                    ) {
                                                        it.cancel()
                                                    }
                                                }
                                            }
                                        }
                                        response.onFailure {
                                            pageLoadingHandler(false)
                                            showAlertDialog(
                                                "Gagal",
                                                "Add story gagal ${it.message}",
                                                "Kembali",
                                            ) {
                                                it.cancel()
                                            }
                                        }
                                    }
                            }
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse =
                            Gson().fromJson(errorBody, AddStoryResponseModel::class.java)
                        showToast(errorResponse.message ?: "")
                        pageLoadingHandler(false)
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun pageLoadingHandler(isLoading: Boolean) {
        binding.apply {
//            etDescription2.isEnabled = !isLoading
//            btnUpload.isEnabled = !isLoading
//            btnGallery.isEnabled = !isLoading
//            btnCamera.isEnabled = !isLoading

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

    private fun showAlertDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        callback: (dialog: DialogInterface) -> Unit,
    ) {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, which ->
                callback(dialog)
            }
            create()
            show()
        }
        alertDialog
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}