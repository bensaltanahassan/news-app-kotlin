package com.example.newsapp

import Crud
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentLoginBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException


class LoginFragment : Fragment() {
    private lateinit var _binding : FragmentLoginBinding
    private val binding get() = _binding!!
    val crud = Crud()
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

        binding.forgotPasswordButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment);

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
        crud.post(loginUrl,json,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("POST Response:", "$responseData")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Request failed:", "${e.message}")
            }
            }
        )
    }

}