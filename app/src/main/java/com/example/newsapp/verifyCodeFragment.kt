package com.example.newsapp

import Crud
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.FragmentSignUpBinding
import com.example.newsapp.databinding.FragmentVerifyCodeBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class verifyCodeFragment : Fragment() {
    val args: verifyCodeFragmentArgs by navArgs()

    private lateinit var _binding : FragmentVerifyCodeBinding
    private lateinit var type:String;
    private val binding get() = _binding!!
    val crud = Crud()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email: String? = args.email
        type = args.type!!
        _binding = FragmentVerifyCodeBinding.inflate(inflater, container, false)
        binding.verifyCodeButton.setOnClickListener{
            val num1 = binding.otp1.text.toString()
            val num2 = binding.otp2.text.toString()
            val num3 = binding.otp3.text.toString()
            val num4 = binding.otp4.text.toString()
            val num5 = binding.otp5.text.toString()
            val concatenatedNumber = "$num1$num2$num3$num4$num5"
            val otpNumber = concatenatedNumber.toInt()
            postVerifyCode(otpNumber,email!!)
        }
        return binding.root
    }

    private fun postVerifyCode(otpNumber:Int,email:String){
        val url : String = "https://news-api-8kaq.onrender.com/api/auth/verifycode"
        val json = """
            {
                "email": "$email",
                "verifyCode": $otpNumber
            }
        """.trimIndent()


        crud.post(url,json,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonresponse = JSONObject(responseData)
                Log.d("POST Response:", "$responseData")
                if(!response.isSuccessful){
                    val message = jsonresponse.getString("message")
                    requireActivity().runOnUiThread{

                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }

                }else{
                    requireActivity().runOnUiThread{
                        if (type=="forgetPassword"){
                            val action = verifyCodeFragmentDirections.actionVerifyCodeFragmentToChangePasswordFragment(email)
                            findNavController().navigate(action)
                        }else{
                            findNavController().navigate(R.id.action_verifyCodeFragment_to_loginFragment)
                        }


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