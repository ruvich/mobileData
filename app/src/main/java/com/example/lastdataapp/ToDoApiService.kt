package com.example.lastdataapp

import retrofit2.Call
import retrofit2.http.*

interface ToDoApiService {
    @GET("api/todo")
    fun getTasks(): Call<List<Item>>

    @POST("api/todo")
    fun createTask(@Body task: Item): Call<Item>

    @DELETE("api/todo/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>

    @PUT("api/todo/{id}")
    fun updateTask(@Path("id") id: Int, @Body task: Item): Call<Void>

    @PATCH("api/todo/{id}/toggle")
    fun toggleTask(@Path("id") id: Int): Call<Void>
}
