package com.example.bijikopiku.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.R
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityResultBinding
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CoffeeFilterDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Coffee
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private lateinit var coffeeList: List<Coffee>
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)
        cartManager = CartManager(this)

        fetchData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData() {
        val isForCoffeeEnthusiast = intent.getBooleanExtra("isForCoffeeEnthusiast", false)
        val coffeeType = intent.getStringExtra("coffeeType") ?: ""
        val taste = intent.getStringExtra("taste") ?: ""
        val isItForSweet = intent.getBooleanExtra("isItForSweet", false)
        val flavor = intent.getStringExtra("flavor") ?: ""

        val filterDTO = CoffeeFilterDTO(
            isForCoffeeEnthusiast = isForCoffeeEnthusiast,
            type = coffeeType,
            isItForSweet = isItForSweet,
            taste = taste,
            flavor = flavor
        )

        val call = apiInterface.getCoffeeFiltered(filterDTO)
        call.enqueue(object : Callback<ApiRes<List<Coffee>>> {
            override fun onResponse(
                call: Call<ApiRes<List<Coffee>>>,
                response: Response<ApiRes<List<Coffee>>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("RESULT", data.toString())
                    if (data?.success == true) {
                        coffeeList = data.data

                        val adapter = CoffeeAdapter(coffeeList, cartManager)
                        binding.rvResultList.layoutManager =
                            GridLayoutManager(this@ResultActivity, 2)
                        binding.rvResultList.adapter = adapter
                    } else {
                        Toast.makeText(this@ResultActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(this@ResultActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<List<Coffee>>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@ResultActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}