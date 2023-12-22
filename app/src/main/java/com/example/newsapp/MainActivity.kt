package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.Coil
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        if (sharedPreferencesManager.isLoggedIn()) {
            navigateToHomeFragment()
        }


    }


    private fun navigateToHomeFragment() {

    }
}