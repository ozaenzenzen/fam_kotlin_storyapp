package com.example.famstoryappkotlin.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.famgithubuser1.data.retrofit.ApiService
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.databinding.ActivityRegisterBinding
import com.example.famstoryappkotlin.ui.home.HomeActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    // private val viewModel: RegisterViewModel by viewModels()
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun registerInputHandler() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        pageLoadingHandler(true)

        lifecycleScope.launch {
            viewModel.register(name, email, password).collect { result ->
                result.onSuccess {
                    showAlertDialog(
                        "Berhasil",
                        "Register Berhasil",
                        "Lanjutkan",
//                        {
////                            finish()
//                        }
                    )
                }
                result.onFailure {
                    showAlertDialog(
                        "Gagal",
                        "Register Gagal",
                        "Kembali",
//                        {
//
//                        }
                    )
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)

        val nameTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterName, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.tvRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)

        val signupButton =
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

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

    private fun pageLoadingHandler(isLoading: Boolean) {
        binding.apply {
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            nameEditText.isEnabled = !isLoading
            signupButton.isEnabled = !isLoading

            if (isLoading) {
                // viewLoading.visibility = true
                viewLoading.apply {
                    ObjectAnimator
                        .ofFloat(this, View.ALPHA, if (true) 1f else 0f)
                        .setDuration(400)
                        .start()
                }
            } else {
                // viewLoading.visibility = false
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
//        callback: () -> Unit,
    ) {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { _, _ ->
//                callback
//                val intent = Intent(context, HomeActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
                finish()
            }
            create()
            show()
        }
        alertDialog
    }
}