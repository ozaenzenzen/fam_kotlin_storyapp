package com.example.famstoryappkotlin.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}