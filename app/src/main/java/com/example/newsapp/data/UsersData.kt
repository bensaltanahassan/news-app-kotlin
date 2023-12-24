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
        image:File?,
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
        if (image!=null){
            Log.d("withImage","withImage")
            Log.d("image","${image.path}")
            crud.putWithImage(
                urlApi,
                json,
                token,
                image,
                object: Crud.ResponseCallback{
                    override fun onResponse(call: Call, response: Response) {
                        val response = response.body?.string()
                        val gson = Gson()
                        val updateUserResponse = gson.fromJson(response, UpdateUserResponse::class.java)
                        Log.d("updateUserResponse",updateUserResponse.toString())
                        onSuccess(updateUserResponse)
                    }
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("updateUserFailure",e.message!!)
                        onFailure(e.message!!)
                    }
                }
            )
        }else{
            Log.d("withoutImage","withoutImage")

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







    }


}