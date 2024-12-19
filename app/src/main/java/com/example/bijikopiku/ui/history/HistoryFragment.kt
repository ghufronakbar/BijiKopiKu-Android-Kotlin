package com.example.bijikopiku.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.adapter.HistoryAdapter
import com.example.bijikopiku.databinding.FragmentHistoryBinding
import com.example.bijikopiku.model.History
import java.util.UUID

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val historyList = listOf(
            History(UUID.randomUUID().toString(), "Arabica", 15000.0, "Completed"),
            History(UUID.randomUUID().toString(), "Robusta", 20000.0, "Pending"),
            History(UUID.randomUUID().toString(), "Liberica", 25000.0, "Completed"),
            History(UUID.randomUUID().toString(), "Excelsa", 30000.0, "Cancelled"),
        )
        val adapter = HistoryAdapter(historyList)
        binding.rvHistoryList.layoutManager = GridLayoutManager(context, 1)
        binding.rvHistoryList.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}