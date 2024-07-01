package com.example.famstoryappkotlin.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.databinding.ActivityHomeBinding
import com.example.famstoryappkotlin.databinding.ActivityMainBinding
import com.example.famstoryappkotlin.ui.addstory.AddStoryActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.toolbar)

        binding.fabCreateStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }
}