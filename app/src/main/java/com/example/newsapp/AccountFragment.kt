package com.example.newsapp

import SharedPreferencesManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.newsapp.data.UsersData
import com.example.newsapp.databinding.FragmentAccountBinding
import com.example.newsapp.databinding.FragmentSavedArticlesBinding
import com.example.newsapp.models.User
import com.example.newsapp.utlis.RealPathUtil
import java.io.File
import java.io.FileInputStream


class AccountFragment : Fragment() {


    private lateinit var _binding : FragmentAccountBinding
    private lateinit var toolbar : Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private val binding get() = _binding!!

    private lateinit var usersData: UsersData
    private var file:File? = null
    private var fileUri:Uri? = null



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //middleware login
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!

        usersData = UsersData(user._id,user.token!!)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        toolbar = binding.appBarProfile.myToolBar
        toolbar.title="Profile"




        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.profilePageDrawer)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                    true
                }
                R.id.savedNewsPageDrawer -> {
                    findNavController().navigate(R.id.action_accountFragment_to_savedArticlesFragment)
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
        //end drawer menu



        //bottom navigation view
        val bottomNavigationView = binding.bottomNavigationView
        val currentDestinationId = findNavController().currentDestination?.id
        bottomNavigationView.selectedItemId = when (currentDestinationId) {
            R.id.homeFragment -> R.id.home
            R.id.accountFragment -> R.id.settings
            R.id.savedArticlesFragment -> R.id.saved
            else -> R.id.home
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    true
                }
                R.id.saved -> {
                    findNavController().navigate(R.id.action_accountFragment_to_savedArticlesFragment)
                    true
                }
                R.id.home -> {
                    findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                    true
                }
                else -> false
            }
        }
        //end bottom navigation view
        binding.firstName.setText(user.firstName)
        binding.lastName.setText(user.lastName)
        binding.email.setText(user.email)


        binding.updateProfileImageBtn.setOnClickListener {
            pickImage()

        }


        binding.updateProfileBtn.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.email.text.toString()
            if (!handlePasswordInput()){
                return@setOnClickListener
            }
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()  ) {
                Toast.makeText(requireContext(), "Vueillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = View.VISIBLE
                binding.updateProfileBtn.visibility = View.GONE

                usersData.updateUser(
                    firstName,
                    lastName,
                    email,
                    binding.password.text.toString(),
                    file,
                    onSuccess = {
                        if (it.status){
                            requireActivity().runOnUiThread {
                                var updatedUser = it.user!!
                                updatedUser.token = user.token
                                sharedPref.saveUser(updatedUser)
                                user = updatedUser
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            requireActivity().runOnUiThread {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            binding.updateProfileBtn.visibility = View.VISIBLE
                        }
                    },
                    onFailure = {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.updateProfileBtn.visibility = View.VISIBLE
                        }
                    }

                )
            }
        }
        return binding.root
    }

    private fun pickImage() {
        if (1==1
        ) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 10)
        }else{
            Log.d("permission","not granted")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==10 && resultCode== Activity.RESULT_OK && data!=null){
            fileUri = data.data
            val path = RealPathUtil.getRealPath(requireContext(),fileUri!!)
            file = File(path)
            val bitmap =BitmapFactory.decodeFile(path)
            binding.avatarView.setImageBitmap(bitmap)
        }
    }


    private fun handlePasswordInput():Boolean{
        val password = binding.password
        if (password.text.toString().isNotEmpty()){
            if (password.text.toString().length < 8){
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_SHORT).show()
                }
                return false
            }else{
                //TODO
            }

        }
        return true


    }




}

