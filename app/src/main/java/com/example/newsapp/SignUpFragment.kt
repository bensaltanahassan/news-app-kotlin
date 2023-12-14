package com.example.newsapp

import Crud
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newsapp.databinding.FragmentSignUpBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONObject


class SignUpFragment : Fragment() {
    private lateinit var _binding : FragmentSignUpBinding
    private val binding get() = _binding!!
    val crud = Crud()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.signUpButton.setOnClickListener{
            val firstName = binding.firstNameSignUp.text.toString()
            val lastName = binding.lastNameSignUp.text.toString()
            val email = binding.emailSignUp.text.toString()
            val password = binding.passwordSignUp.text.toString()
            val confirmPassword = binding.confirmPasswordSignUp.text.toString()
            if(password == confirmPassword){
                postLogin(firstName,lastName,email,password)
            }else{
                Toast.makeText(requireContext(), "Passwords doesnt match !", Toast.LENGTH_LONG).show()
            }

        }
        return binding.root
    }

    private fun postLogin(firstName : String ,lastName : String,email : String, password : String){
        val loginUrl : String = "https://news-api-8kaq.onrender.com/api/auth/signup"
        val json = """
            {
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()


        crud.post(loginUrl,json,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonresponse = JSONObject(responseData)
                Log.d("POST Response:", "$responseData")
                if(!response.isSuccessful){
                    val message = jsonresponse.getString("message")
                    requireActivity().runOnUiThread{

                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }

                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Request failed:", "${e.message}")

            }
        }
        )
    }

}