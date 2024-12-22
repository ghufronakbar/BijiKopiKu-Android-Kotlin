package com.example.bijikopiku.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.LoginActivity
import com.example.bijikopiku.SearchActivity
import com.example.bijikopiku.adapter.CoffeeAdapter
import com.example.bijikopiku.databinding.FragmentHomeBinding
import com.example.bijikopiku.model.Coffee

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val coffeeList = listOf(
            Coffee("B1", "Arabica", 15000, "Whole Bean", "Rich"),
            Coffee("B2", "Robusta", 20000, "Ground", "Bold"),
            Coffee("B3", "Liberica", 25000, "Whole Bean", "Fruity"),
            Coffee("B4", "Excelsa", 30000, "Ground", "Tart"),
            Coffee("B5", "Blend", 22000, "Whole Bean", "Balanced")
        )

        val adapter = CoffeeAdapter(coffeeList)
        binding.rvCoffeeList.layoutManager = GridLayoutManager(context, 2)
        binding.rvCoffeeList.adapter = adapter


        binding.ivCart.setOnClickListener {
            onCartClick()
        }

        binding.cvSearch.setOnClickListener{
            val intent = Intent()
            intent.setClass(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun onCartClick(){
        val intent = Intent()
        intent.setClass(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}