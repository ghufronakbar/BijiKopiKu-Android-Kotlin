package com.example.bijikopiku.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bijikopiku.R
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityDetailCoffeeBinding
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CartItem
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Coffee
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DetailCoffeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCoffeeBinding

    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface
    private lateinit var cartManager: CartManager

    companion object {
        const val ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCoffeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        supportActionBar?.hide()

        val id = intent.getStringExtra(ID) ?: ""

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)
        cartManager = CartManager(this)

        fetchData(id)

        binding.btnAddToCart.setOnClickListener {
            addToCart(id)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData(id: String) {
        val call = apiInterface.getCoffee(id)
        call.enqueue(object : Callback<ApiRes<Coffee>> {
            override fun onResponse(
                call: Call<ApiRes<Coffee>>,
                response: Response<ApiRes<Coffee>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {

                        binding.tvName.text = "${data.data.name} | ${data.data.type}"
                        binding.tvPrice.text = "Rp ${data.data.price}"
                        binding.tvSold.text = "Terjual: ${data.data.sold}"
                        binding.tvTaste.text = "Tipe Rasa: ${data.data.taste}"
                        binding.tvFlavor.text = "Karakteristik Rasa: ${data.data.flavor}"
                        binding.tvDesc.text = data.data.desc

                        Glide.with(this@DetailCoffeeActivity)
                            .load(data.data.picture)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(binding.ivCoffeeImage)


                        if (data.data.isItForSweet) {
                            binding.tvIsItForSweet.text = "Cocok untuk ditambah pemanis"
                        } else {
                            binding.tvIsItForSweet.text = "Tidak cocok untuk ditambah pemanis"
                        }

                        if (data.data.isForCoffeeEnthusiast) {
                            binding.tvIsForCoffeeEnthusiast.text = "Cocok untuk Coffee Enthusiast"
                        } else {
                            binding.tvIsForCoffeeEnthusiast.text = "Cocok untuk pemula kopi"
                        }

                        binding.progressBar.visibility = View.GONE
                        binding.btnAddToCart.visibility = View.VISIBLE
                        binding.lnContent.visibility = View.VISIBLE

                    } else {
                        Toast.makeText(this@DetailCoffeeActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@DetailCoffeeActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()

                }
            }

            override fun onFailure(call: Call<ApiRes<Coffee>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@DetailCoffeeActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        })
    }

    private fun addToCart(id: String) {
        val checkCart = cartManager.findItemById(id)
        var quantity = 1
        if (checkCart != null) {
            quantity = checkCart.quantity + 1
        }
        val cartItem = CartItem(id, quantity)
        cartManager.addToCartOrUpdateQuantity(cartItem)
        Toast.makeText(this, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        finish()
    }
}