package com.example.famstoryappkotlin.ui.views.login

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
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.databinding.ActivityLoginBinding
import com.example.famstoryappkotlin.ui.views.home.HomeActivity
import com.example.famstoryappkotlin.ui.views.register.RegisterViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        ).get(LoginViewModel::class.java)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView =
            ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val messageTextView =
            ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(300)

        val emailTextView =
            ObjectAnimator.ofFloat(binding.tvLoginEmail, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)

        val passwordTextView =
            ObjectAnimator.ofFloat(binding.tvLoginPassword, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(300)

        val loginButton =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                messageTextView,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                loginButton,
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
        binding.loginButton.setOnClickListener {
            loginInputHandler()
        }
    }

    private fun loginInputHandler() {
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()
        pageLoadingHandler(true)

        lifecycleScope.launch {
            viewModel.login(email, password).collect { result ->
                result.onSuccess { data ->
                    data.loginResult?.token?.let { token ->
                        showAlertDialog(
                            "Berhasil",
                            "Login Berhasil",
                            "Lanjutkan"
                        ) {
                            viewModel.saveAuthenticationToken(token)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                }
                result.onFailure {
                    pageLoadingHandler(false)
                    showAlertDialog(
                        "Gagal",
                        "Login Gagal",
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
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
            loginButton.isEnabled = !isLoading

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