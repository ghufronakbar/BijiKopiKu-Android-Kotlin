package com.example.bijikopiku.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.R
import com.example.bijikopiku.adapter.CartAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityCartBinding
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CartDTO
import com.example.bijikopiku.model.dto.CartItem
import com.example.bijikopiku.model.dto.CheckoutDTO
import com.example.bijikopiku.model.dto.CheckoutItem
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Coffee
import com.example.bijikopiku.model.response.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var coffeeList: MutableList<Coffee>
    private lateinit var items: List<CartItem>
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)
        cartManager = CartManager(this)

        fetchData()

        binding.btnCheckout.setOnClickListener {
            handleCheckout()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData() {

        items = cartManager.getCart()

        if (items.isEmpty()) {
            handleEmptyData()
            return
        }

        var requestBody = CartDTO(
            items
        )

        val call = apiInterface.getCarts(requestBody)
        call.enqueue(object : Callback<ApiRes<List<Coffee>>> {
            override fun onResponse(
                call: Call<ApiRes<List<Coffee>>>,
                response: Response<ApiRes<List<Coffee>>>
            ) {

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        val newCart = data.data.map { CartItem(it.id, it.quantity) }
                        cartManager.replaceCart(newCart)
                        if (data.data.isEmpty()) {
                            handleEmptyData()
                            return
                        }
                        binding.tvInformation.visibility = View.GONE
                        coffeeList = data.data.toMutableList()
                        var total = 0
                        for (coffee in coffeeList) {
                            total += coffee.price.toInt() * coffee.quantity
                        }
                        binding.tvTotal.text = "Rp $total"
                        val adapter = CartAdapter(coffeeList, cartManager, total, { updatedTotal ->
                            binding.tvTotal.text = "Rp $updatedTotal"
                        }, {
                            handleEmptyData()
                        })
                        binding.rvCartList.layoutManager = GridLayoutManager(this@CartActivity, 1)
                        binding.rvCartList.adapter = adapter
                    } else {
                        Toast.makeText(this@CartActivity, data?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<List<Coffee>>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleEmptyData() {
        binding.tvTotal.text = "Rp 0"
        binding.tvInformation.visibility = View.VISIBLE
        binding.tvInformation.text = "Keranjang belanja kosong"
    }

    private fun handleCheckout() {
        if (checkAccessToken()) {
            if (cartManager.getCart().isEmpty()) {
                Toast.makeText(this, "Keranjang belanja kosong", Toast.LENGTH_SHORT).show()
                return
            } else {
                doCheckout()
            }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return
        }
    }

    private fun checkAccessToken(): Boolean {
        val preferences: SharedPreferences =
            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("ACCESS_TOKEN", null)
        if (token == null) {
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun doCheckout() {

        val cart: List<CartItem> = cartManager.getCart()
        val checkoutItems = cart.map {
            CheckoutItem(it.id, it.quantity)
        }
        val body = CheckoutDTO(orderItems = checkoutItems)
        val call = apiInterface.createCheckout(body)
        Toast.makeText(this, "Harap tunggu sebentar", Toast.LENGTH_SHORT).show()
        call.enqueue(object : Callback<ApiRes<Order>> {
            override fun onResponse(
                call: Call<ApiRes<Order>>,
                response: Response<ApiRes<Order>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        Toast.makeText(this@CartActivity, "Checkout berhasil", Toast.LENGTH_SHORT)
                            .show()
                        cartManager.clearCart()
                        val intent = Intent(this@CartActivity, DetailOrderActivity::class.java)
                        intent.putExtra("id", data.data.id)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@CartActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@CartActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}