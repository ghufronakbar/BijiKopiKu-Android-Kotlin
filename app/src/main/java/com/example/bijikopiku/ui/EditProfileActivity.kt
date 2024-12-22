package com.example.bijikopiku.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityEditProfileBinding
import com.example.bijikopiku.model.dto.ProfileDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface
    private var isFetchData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)

        fetchData()

        binding.btnSaveProfile.setOnClickListener {
            handleUpdateProfile()
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData() {
        val call = apiInterface.getProfile()
        call.enqueue(object : Callback<ApiRes<User>> {
            override fun onResponse(
                call: Call<ApiRes<User>>,
                response: Response<ApiRes<User>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {

                        binding.etProfileEmail.setText(data.data.email)
                        binding.etProfileName.setText(data.data.name)
                        binding.etProfilePhone.setText(data.data.phone)
                        isFetchData = true

                    } else {
                        Toast.makeText(this@EditProfileActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<User>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun handleUpdateProfile() {
        if (!isFetchData) {
            return
        }
        val email = binding.etProfileEmail.text.toString()
        val name = binding.etProfileName.text.toString()
        val phone = binding.etProfilePhone.text.toString()
        if (email.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val body = ProfileDTO(name = name, email = email, phone = phone)
        val call = apiInterface.editProfile(body)
        call.enqueue(object : Callback<ApiRes<User>> {
            override fun onResponse(
                call: Call<ApiRes<User>>,
                response: Response<ApiRes<User>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        Toast.makeText(this@EditProfileActivity, data.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@EditProfileActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<User>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}