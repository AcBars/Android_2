package com.example.android20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    interface ApiService {
        @POST("/login")
        fun register(@Body body: JsonObject): Call<JsonObject>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.29:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val jsonObject = JsonObject().apply {
                addProperty("username", email)
                addProperty("password", password)
            }

            apiService.register(jsonObject).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body()?.has("id") == true) {
                        val intent = Intent(this@MainActivity, SuccessActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Переделывай", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Ошибка сети", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
