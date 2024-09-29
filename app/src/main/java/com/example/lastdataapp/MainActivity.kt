package com.example.lastdataapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastdataapp.RetrofitInstance

class MainActivity : AppCompatActivity() {
    private val allTasks = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter
    private val apiService = RetrofitInstance.api

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = findViewById<EditText>(R.id.userData)
        val createButton = findViewById<Button>(R.id.buttonCreate)
        val recyclerView = findViewById<RecyclerView>(R.id.listOfItems)

        adapter = ItemAdapter(allTasks) { position -> deleteTask(allTasks[position].id, position) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        createButton.setOnClickListener {
            val text = userData.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "You have not entered any text!", Toast.LENGTH_SHORT).show()
            } else {
                val newItem = Item(isChecked = false, taskText = text, id = 0)
                createTask(newItem)
                userData.text.clear()
            }
        }

        loadTasks()
    }

    private fun loadTasks() {
        apiService.getTasks().enqueue(object : retrofit2.Callback<List<Item>> {
            override fun onResponse(call: retrofit2.Call<List<Item>>, response: retrofit2.Response<List<Item>>) {
                if (response.isSuccessful) {
                    allTasks.clear()
                    response.body()?.let { allTasks.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load tasks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Item>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createTask(task: Item) {
        apiService.createTask(task).enqueue(object : retrofit2.Callback<Item> {
            override fun onResponse(call: retrofit2.Call<Item>, response: retrofit2.Response<Item>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        allTasks.add(it)
                        adapter.notifyItemInserted(allTasks.size - 1)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to create task", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Item>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteTask(id: Int, position: Int) {
        apiService.deleteTask(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    allTasks.removeAt(position)
                    adapter.notifyItemRemoved(position)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to delete task", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleTask(id: Int, position: Int) {
        apiService.toggleTask(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    allTasks[position].isChecked = !allTasks[position].isChecked
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to toggle task", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTask(task: Item) {
        apiService.updateTask(task.id, task).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    val position = allTasks.indexOfFirst { it.id == task.id }
                    if (position != -1) {
                        allTasks[position] = task
                        adapter.notifyItemChanged(position)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update task", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
