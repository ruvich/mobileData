package com.example.lastdataapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.202:7122/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ToDoApiService = retrofit.create(ToDoApiService::class.java)
}