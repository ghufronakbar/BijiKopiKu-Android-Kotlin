package com.example.bijikopiku.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bijikopiku.R
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityRegisterBinding
import com.example.bijikopiku.model.dto.RegisterDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.User
import retrofit2.Call
import retrofit2.Retrofit

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)

        binding.btnRegister.setOnClickListener {
            Log.d("REGISTER", "Register button clicked")
            handleRegister()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleRegister() {
        val name = binding.etRegisterName.text.toString()
        val email = binding.etRegisterEmail.text.toString()
        val password = binding.etRegisterPass.text.toString()
        val confirmPass = binding.etRegisterConfirmPass.text.toString()
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }
        if(password.length < 6){
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != confirmPass){
            Toast.makeText(this, "Konfirmasi password tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        val body = RegisterDTO(name, email, "  ", password)
        val call = apiInterface.postRegister(body)
        call.enqueue(object : retrofit2.Callback<ApiRes<User>> {
            override fun onResponse(
                call: Call<ApiRes<User>>,
                response: retrofit2.Response<ApiRes<User>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        Toast.makeText(this@RegisterActivity, data.message, Toast.LENGTH_SHORT)
                            .show()
                        val sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("ACCESS_TOKEN", data.data?.accessToken)
                        editor.apply()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<User>>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}