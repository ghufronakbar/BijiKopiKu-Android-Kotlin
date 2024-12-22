package com.example.bijikopiku.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityLoginBinding
import com.example.bijikopiku.model.dto.LoginDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.User
import retrofit2.Call
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleLogin() {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPass.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }
        val body = LoginDTO(email, password)
        val call = apiInterface.postLogin(body)
        call.enqueue(object : retrofit2.Callback<ApiRes<User>> {
            override fun onResponse(call: Call<ApiRes<User>>, response: retrofit2.Response<ApiRes<User>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_SHORT).show()
                        val sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        Log.d("ACCESS_TOKEN", data.data?.accessToken.toString())
                        editor.putString("ACCESS_TOKEN", data.data?.accessToken)
                        editor.apply()
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, data?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiRes<User>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}