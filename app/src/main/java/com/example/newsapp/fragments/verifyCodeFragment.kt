package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.data.AuthData
import com.example.newsapp.databinding.FragmentVerifyCodeBinding

class verifyCodeFragment : Fragment() {
    val args: verifyCodeFragmentArgs by navArgs()

    private lateinit var _binding : FragmentVerifyCodeBinding
    private lateinit var type:String;
    private val binding get() = _binding!!
    private val authData:AuthData = AuthData()
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
            try {
                val otpNumber = concatenatedNumber.toInt()
                binding.progressBar.visibility = View.VISIBLE
                binding.verifyCodeButton.visibility = View.GONE
                verifyCode(otpNumber,email!!)
            }
            catch (e: NumberFormatException){
                Toast.makeText(requireContext(), "Véulliez saisir un code valide", Toast.LENGTH_LONG).show()
            }

        }
        return binding.root
    }

    private fun verifyCode (otpNumber:Int,email:String){
        authData.verifyCode(
            email,
            otpNumber,
            onSuccess = { verifyCodeResponse ->
                if (verifyCodeResponse.status) {
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.verifyCodeButton.visibility = View.VISIBLE
                        if (type=="forgetPassword"){
                            val action =
                                verifyCodeFragmentDirections.actionVerifyCodeFragmentToChangePasswordFragment(
                                    email
                                )
                            findNavController().navigate(action)
                        }else{
                            findNavController().navigate(R.id.action_verifyCodeFragment_to_loginFragment)
                        }
                    }
                }else{
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.verifyCodeButton.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), verifyCodeResponse.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.verifyCodeButton.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        )

    }




}