package com.example.newsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.databinding.FragmentForgotPasswordBinding
import com.example.newsapp.databinding.FragmentLoginBinding

class forgotPasswordFragment : Fragment() {
    private lateinit var _binding : FragmentForgotPasswordBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)


        return return binding.root
    }


}