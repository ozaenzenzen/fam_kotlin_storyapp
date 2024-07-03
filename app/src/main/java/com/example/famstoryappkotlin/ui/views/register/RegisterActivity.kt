package com.example.famstoryappkotlin.ui.views.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    // private val viewModel: RegisterViewModel by viewModels()
    private lateinit var viewModel: RegisterViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        var pref = UserDataPreferences.getInstance(application.dataStore)

        var apiService = ApiConfig.getApiService()

        authRepository = AuthRepository(apiService, pref)
        storyRepository = StoryRepository(apiService)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(authRepository, storyRepository)
        ).get(RegisterViewModel::class.java)
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)

        val nameTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterName, View.ALPHA, 1f).setDuration(300)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(300)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(300)

        val signupButton =
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(300)

        val togetherTitle = AnimatorSet().apply {
            playTogether(nameTextView, nameEditTextLayout)
        }
        val togetherEmail = AnimatorSet().apply {
            playTogether(emailTextView, emailEditTextLayout)
        }
        val togetherPassword = AnimatorSet().apply {
            playTogether(passwordTextView, passwordEditTextLayout)
        }

        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                togetherTitle,
                togetherEmail,
                togetherPassword,
                signupButton
            )
            start()
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
//            val email = binding.emailEditText.text.toString()
//
//            AlertDialog.Builder(this).apply {
//                setTitle("Yeah!")
//                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
//                setPositiveButton("Lanjut") { _, _ ->
//                    finish()
//                }
//                create()
//                show()
//            }
            registerInputHandler()
        }
    }

    private fun registerInputHandler() {
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        pageLoadingHandler(true)

        lifecycleScope.launch {
            viewModel.register(name, email, password).collect { result ->
                result.onSuccess {
                    showAlertDialog(
                        "Berhasil",
                        "Register Berhasil",
                        "Lanjutkan"
                    ) {
                        finish()
                    }
                }
                result.onFailure {
                    pageLoadingHandler(false)
                    showAlertDialog(
                        "Gagal",
                        "Register Gagal",
                        "Kembali",
                    ) { dialog ->
                        dialog.cancel()
                    }
                }
            }
        }
    }

    private fun pageLoadingHandler(isLoading: Boolean) {
        binding.apply {
            edRegisterEmail.isEnabled = !isLoading
            edRegisterPassword.isEnabled = !isLoading
            edRegisterName.isEnabled = !isLoading
            signupButton.isEnabled = !isLoading

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
}