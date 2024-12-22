package com.example.bijikopiku.ui

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
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.adapter.DetailHistoryAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityDetailOrderBinding
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.LoginDTO
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Order
import com.example.bijikopiku.model.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding

    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    companion object {
        const val ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        supportActionBar?.hide()

        val id = intent.getStringExtra(DetailCoffeeActivity.ID) ?: ""

        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)

        fetchData(id)

        binding.btnCancel.setOnClickListener {
            handleCancel(id)
        }


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData(id: String) {
        val call = apiInterface.getDetailOrder(id)
        call.enqueue(object : Callback<ApiRes<Order>> {
            override fun onResponse(
                call: Call<ApiRes<Order>>,
                response: Response<ApiRes<Order>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {

                        binding.tvOrderId.text = "#${data.data.id}$"
                        binding.tvTotal.text = "Rp ${data.data.total}"
                        binding.tvDate.text = data.data.createdAt
                        binding.tvStatus.text = data.data.status

                        binding.progressBar.visibility = View.GONE
                        binding.lnContent.visibility = View.VISIBLE

                        when (data.data.status) {
                            "Dipesan" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.gray
                                    )
                                )
                                binding.btnCancel.visibility = View.VISIBLE
                                binding.btnPay.visibility = View.VISIBLE
                            }

                            "Dibayar" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.yellow
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }

                            "Dibatalkan" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.orange
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }

                            "Ditolak" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.red
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }

                            "Diterima" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.green
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }

                            "Selesai" -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.primary
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }

                            else -> {
                                binding.badgeStatus.setCardBackgroundColor(
                                    getColor(
                                        R.color.gray
                                    )
                                )
                                binding.btnCancel.visibility = View.GONE
                                binding.btnPay.visibility = View.GONE
                            }
                        }

                        val adapter = DetailHistoryAdapter(data.data.orderItems)
                        binding.rvItemDetailOrder.layoutManager =
                            GridLayoutManager(this@DetailOrderActivity, 1)
                        binding.rvItemDetailOrder.adapter = adapter

                    } else {
                        Toast.makeText(this@DetailOrderActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@DetailOrderActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()

                }
            }

            override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@DetailOrderActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        })
    }

    private fun handleCancel(id: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembatalan")
        builder.setMessage("Apakah Anda yakin ingin membatalkan pesanan ini?")

        builder.setPositiveButton("Ya") { _, _ ->
            val call = apiInterface.cancelOrder(id)
            Toast.makeText(this, "Harap tunggu sebentar", Toast.LENGTH_SHORT).show()
            call.enqueue(object : Callback<ApiRes<Order>> {
                override fun onResponse(call: Call<ApiRes<Order>>, response: Response<ApiRes<Order>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success == true) {
                            Toast.makeText(this@DetailOrderActivity, data.message, Toast.LENGTH_SHORT)
                                .show()
                            fetchData(id)
                        } else {
                            Toast.makeText(this@DetailOrderActivity, data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailOrderActivity,
                            "Terjadi kesalahan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                    Toast.makeText(this@DetailOrderActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}