package com.example.newsapp.data

import Crud
import android.util.Log
import com.example.newsapp.utlis.UpdateUserResponse
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import java.io.File

class UsersData(private var userId: String, private var token: String) {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"

    fun updateUser(
        firstName:String?,
        lastName:String?,
        email:String?,
        password:String?,
        onSuccess : (UpdateUserResponse) -> Unit,
        onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/users/$userId"
        var json:String = ""

        if (password!=null){
            json = """
            {
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        }else{
            json = """
            {
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email"
            }
        """.trimIndent()
        }


        crud.update(
            urlApi,
            json,
            token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val updateUserResponse = gson.fromJson(response, UpdateUserResponse::class.java)
                    onSuccess(updateUserResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


    fun updateUserWithImage(
        firstName:String?,
        lastName:String?,
        email:String?,
        password:String?,
        file:File,
        onSuccess : (UpdateUserResponse) -> Unit,
        onFailure : (String) -> Unit
    ){
        val urlApi  = "$baseUrl/users/$userId"


        crud.putWithImage(
            urlApi,
            firstName!!,
            lastName!!,
            email!!,
            password!!,
            token,
            file,

            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val updateUserResponse = gson.fromJson(response, UpdateUserResponse::class.java)
                    Log.d("updateUserWithImage",updateUserResponse.toString())
                    onSuccess(updateUserResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


}