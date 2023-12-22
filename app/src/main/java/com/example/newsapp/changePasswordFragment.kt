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
import com.example.newsapp.data.AuthData
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
    private  lateinit var email: String;
    private val authData:AuthData = AuthData()



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
                binding.progressBar.visibility = View.VISIBLE
                binding.changePasswordButton.visibility = View.GONE
                onClickChangePasswordBtn(newPassword,email)
            }else{
                val message : String = "Validation err"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }

        }

        return return binding.root
    }

    private fun onClickChangePasswordBtn(newPassword:String,email:String){
        authData.updatePassword(
            email,
            newPassword,
            onSuccess = { updatePasswordResponse ->
                if (updatePasswordResponse.status) {
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.changePasswordButton.visibility = View.VISIBLE
                        val action = changePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment()
                        findNavController().navigate(action);
                    }
                }else{
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.changePasswordButton.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),updatePasswordResponse.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.changePasswordButton.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        )
    }


}