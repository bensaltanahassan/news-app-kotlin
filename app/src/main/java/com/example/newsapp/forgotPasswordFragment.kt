package com.example.newsapp

import Crud
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentForgotPasswordBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class forgotPasswordFragment : Fragment() {
    private lateinit var _binding : FragmentForgotPasswordBinding
    private val binding get() = _binding!!
    val crud = Crud()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.forgetPaswordButton.setOnClickListener {
            val email: String = binding.emailForgetPassword.text.toString();
            onClickForgetPasswordBtn(email)
        }

        return return binding.root
    }

    private fun onClickForgetPasswordBtn(email:String){
        val forgetPasswordUrl = "https://news-api-8kaq.onrender.com/api/auth/forgetpassword"
        val json = """
            {
                "email": "$email"
            }
        """.trimIndent()
        crud.post(forgetPasswordUrl,json,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonResponse = JSONObject(responseData)

                if (response.isSuccessful) {
                    val message : String = "Un code de verification a été envoyé a $email"
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            val action = forgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerifyCodeFragment(email)
                            findNavController().navigate(action);
                        }, 2000)
                    }

                }else{
                    val message:String = jsonResponse.getString("message")
                    requireActivity().runOnUiThread {
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