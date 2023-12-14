package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.databinding.FragmentLoginBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException


class LoginFragment : Fragment() {
    private lateinit var _binding : FragmentLoginBinding
    private val binding get() = _binding!!
    private val client = OkHttpClient()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginButton.setOnClickListener {
            val email: String = binding.username.text.toString();
            val password: String = binding.password.text.toString()

            postLogin(email,password)


        }
        return binding.root
    }
    private fun postLogin(email : String, password : String){
        val loginUrl : String = "https://news-api-8kaq.onrender.com/api/auth/login"
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val postBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val postRequest = Request.Builder()
            .url(loginUrl)
            .post(postBody)
            .build()

        client.newCall(postRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("POST Response:", "$responseData")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Request failed:", "${e.message}")
            }
        })

    }



    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
    }


}