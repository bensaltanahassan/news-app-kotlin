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
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.FragmentChangePasswordBinding
import com.example.newsapp.databinding.FragmentForgotPasswordBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONObject


class changePasswordFragment : Fragment() {
    val args: changePasswordFragmentArgs by navArgs()

    private lateinit var _binding : FragmentChangePasswordBinding
    private val binding get() = _binding!!
    val crud = Crud()
    private  lateinit var email: String;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        email = args.email!!
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.changePasswordButton.setOnClickListener {
            val newPassword: String = binding.password.text.toString();
            val confirmPassword:String  = binding.confirmPassword.text.toString();
            if (newPassword==confirmPassword){
                onClickForgetPasswordBtn(newPassword,email)
            }else{
                val message : String = "Validation err"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }

        }

        return return binding.root
    }

    private fun onClickForgetPasswordBtn(newPassword:String,email:String){
        val changePasswordUrl = "https://news-api-8kaq.onrender.com/api/auth/changepassword"
        val json = """
            {
                "email": "$email",
                "password": "$newPassword"
            }
        """.trimIndent()
        val token:String = ""
        crud.post(changePasswordUrl,json,token,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonResponse = JSONObject(responseData)

                if (response.isSuccessful) {
                    val message : String = "Votre mot de passe a été changé avec succes"
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().navigate(R.id.action_changePasswordFragment_to_loginFragment)
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