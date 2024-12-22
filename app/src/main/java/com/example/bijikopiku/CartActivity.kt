package com.example.bijikopiku

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.adapter.CartAdapter
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.databinding.ActivityCartBinding
import com.example.bijikopiku.model.Coffee
import com.example.bijikopiku.model.CoffeeCart

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var coffeeList: List<CoffeeCart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()

        fetchData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchData() {
        var data = ArrayList<CoffeeCart>()
        data.add(CoffeeCart("B1", "Arabica", 15000, "Whole Bean", "Rich", 5))
        data.add(CoffeeCart("B2", "Robusta", 20000, "Ground", "Bold", 3))
        data.add(CoffeeCart("B3", "Liberica", 25000, "Whole Bean", "Fruity", 2))
        data.add(CoffeeCart("B4", "Excelsa", 30000, "Ground", "Tart", 9))
        data.add(CoffeeCart("B5", "Blend", 22000, "Whole Bean", "Balanced", 1))
        coffeeList = data

        val adapter = CartAdapter(coffeeList)
        binding.rvCartList.layoutManager = GridLayoutManager(this, 1)
        binding.rvCartList.adapter = adapter
    }
}