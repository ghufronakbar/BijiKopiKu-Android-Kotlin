package com.example.bijikopiku.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.ui.CartActivity
import com.example.bijikopiku.ui.SearchActivity
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.FragmentHomeBinding
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Coffee
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var coffeeList: List<Coffee>
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface
    private lateinit var cartManager: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        retrofit = RetrofitClient.getClient(requireContext())
        apiInterface = retrofit.create(ApiInterface::class.java)

        cartManager = CartManager(requireContext())

        fetchData()

        binding.ivCart.setOnClickListener {
            onCartClick()
        }

        binding.cvSearch.setOnClickListener{
            onSearchClick()
        }

        return root
    }

    private fun onCartClick(){
        val intent = Intent()
        intent.setClass(requireContext(), CartActivity::class.java)
        startActivity(intent)
    }

    private fun onSearchClick(){
        val intent = Intent()
        intent.setClass(requireContext(), SearchActivity::class.java)
        startActivity(intent)
    }

    private fun fetchData() {
        val call = apiInterface.getCoffees()
        call.enqueue(object : Callback<ApiRes<List<Coffee>>> {
            override fun onResponse(
                call: Call<ApiRes<List<Coffee>>>,
                response: Response<ApiRes<List<Coffee>>>
            ) {
                if (isAdded && view != null) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success == true) {
                            coffeeList = data.data

                            val adapter = CoffeeAdapter(coffeeList, cartManager)
                            binding.rvCoffeeList.layoutManager = GridLayoutManager(context, 2)
                            binding.rvCoffeeList.adapter = adapter
                        } else {
                            Toast.makeText(requireContext(), data?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Error", response.errorBody().toString())
                        Toast.makeText(requireContext(), "Terjadi kesalahan1", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiRes<List<Coffee>>>, t: Throwable) {
                if (isAdded && view != null) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(requireContext(), "Terjadi kesalahan2", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}