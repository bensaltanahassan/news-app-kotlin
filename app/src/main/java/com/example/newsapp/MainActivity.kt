package com.example.newsapp

import SharedPreferencesManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.Coil
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.models.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}