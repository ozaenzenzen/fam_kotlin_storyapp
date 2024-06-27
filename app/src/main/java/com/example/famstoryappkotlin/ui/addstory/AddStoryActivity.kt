package com.example.famstoryappkotlin.ui.addstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar("Create Story")
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}