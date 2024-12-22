package com.example.bijikopiku.ui.history

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.adapter.HistoryAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.FragmentHistoryBinding
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!
    private lateinit var historyList: List<Order>
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        retrofit = RetrofitClient.getClient(requireContext())
        apiInterface = retrofit.create(ApiInterface::class.java)

        checkAccessToken()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAccessToken() {
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("ACCESS_TOKEN", null)
        if (token == null) {
            binding.tvInformation.visibility = View.VISIBLE
            binding.tvInformation.text = "Silahkan login terlebih dahulu"
        } else {
            fetchData()
        }
    }

    private fun fetchData() {
        val call = apiInterface.getOrders()
        call.enqueue(object : Callback<ApiRes<List<Order>>> {
            override fun onResponse(
                call: Call<ApiRes<List<Order>>>,
                response: Response<ApiRes<List<Order>>>
            ) {
                if (isAdded && view != null) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("RESULT_ORDER", data.toString())
                        if (data?.success == true) {
                            historyList = data.data

                            if (historyList.isEmpty()) {
                                binding.tvInformation.visibility = View.VISIBLE
                                binding.tvInformation.text = "Tidak ada riwayat transaksi"
                                return
                            }

                            binding.tvInformation.visibility = View.GONE
                            binding.rvHistoryList.visibility = View.VISIBLE

                            val adapter = HistoryAdapter(historyList)
                            binding.rvHistoryList.layoutManager = GridLayoutManager(context, 1)
                            binding.rvHistoryList.adapter = adapter
                        } else {
                            Toast.makeText(requireContext(), data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Log.e("Error", response.errorBody().toString())
                        Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiRes<List<Order>>>, t: Throwable) {
                if (isAdded && view != null) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}