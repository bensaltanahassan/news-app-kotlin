package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newsapp.data.AuthData
import com.example.newsapp.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private lateinit var _binding : FragmentSignUpBinding
    private val binding get() = _binding!!
    private val  authData:AuthData = AuthData()

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
                binding.progressBar.visibility = View.VISIBLE
                binding.signUpButton.visibility = View.GONE
                signUp(firstName,lastName,email,password)
            }else{
                Toast.makeText(requireContext(), "Les mots de passe doivent se rassembler !", Toast.LENGTH_LONG).show()
            }

        }
        binding.signupText.setOnClickListener{
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }
        return binding.root
    }

    private fun signUp(firstName : String ,lastName : String,email : String, password : String){
        authData.signUp(
            firstName,
            lastName,
            email,
            password,
            onSuccess = { signUpResponse ->
                if (signUpResponse.status) {
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.signUpButton.visibility = View.VISIBLE
                        val action =
                            com.example.newsapp.fragments.SignUpFragmentDirections.actionSignUpFragmentToVerifyCodeFragment(
                                email,
                                "signUp"
                            )
                        findNavController().navigate(action)
                    }
                }else{
                    requireActivity().runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        binding.signUpButton.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), signUpResponse.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.signUpButton.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        )

    }


}