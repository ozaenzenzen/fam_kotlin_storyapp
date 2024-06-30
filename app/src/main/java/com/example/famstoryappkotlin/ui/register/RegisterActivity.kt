package com.example.famstoryappkotlin.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}