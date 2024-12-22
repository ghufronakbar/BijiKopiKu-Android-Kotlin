package com.example.bijikopiku

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.databinding.ActivityResultBinding
import com.example.bijikopiku.model.Coffee

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var coffeeList: List<Coffee> 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
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
        var data = ArrayList<Coffee>()
        data.add(Coffee("B1", "Arabica", 15000, "Whole Bean", "Rich"))
        data.add(Coffee("B2", "Robusta", 20000, "Ground", "Bold"))
        data.add(Coffee("B3", "Liberica", 25000, "Whole Bean", "Fruity"))
        data.add(Coffee("B4", "Excelsa", 30000, "Ground", "Tart"))
        data.add(Coffee("B5", "Blend", 22000, "Whole Bean", "Balanced"))
        coffeeList = data

        val adapter = CoffeeAdapter(coffeeList)
        binding.rvResultList.layoutManager = GridLayoutManager(this, 2)
        binding.rvResultList.adapter = adapter
    }
}