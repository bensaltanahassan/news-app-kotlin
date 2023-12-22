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
import com.example.newsapp.data.AuthData
import com.example.newsapp.databinding.FragmentForgotPasswordBinding
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class forgotPasswordFragment : Fragment() {
    private lateinit var _binding : FragmentForgotPasswordBinding
    private val binding get() = _binding!!
    private val authData:AuthData = AuthData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.forgetPaswordButton.setOnClickListener {
            val email: String = binding.emailForgetPassword.text.toString();
            binding.progressBar.visibility = View.VISIBLE
            binding.forgetPaswordButton.visibility = View.GONE

            onClickForgetPasswordBtn(email)
        }

        return return binding.root
    }


    fun onClickForgetPasswordBtn (email:String){
        authData.forgetPassword(
            email,
            onSuccess = { forgetPasswordResponse ->
                if (forgetPasswordResponse.status) {
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.forgetPaswordButton.visibility = View.VISIBLE
                        val action = forgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerifyCodeFragment(email,"forgetPassword")
                        findNavController().navigate(action);
                    }
                }else{
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.forgetPaswordButton.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),forgetPasswordResponse.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.forgetPaswordButton.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        )
    }




}