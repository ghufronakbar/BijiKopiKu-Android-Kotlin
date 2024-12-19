package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bijikopiku.R
import com.example.bijikopiku.model.History

class HistoryAdapter(private val historyList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hisId : TextView = itemView.findViewById(R.id.tvHistoryId)
        val hisImage: ImageView = itemView.findViewById(R.id.ivHistoryImage)
        val hisName: TextView = itemView.findViewById(R.id.tvHistoryName)
        val hisTotal: TextView = itemView.findViewById(R.id.tvHistoryTotal)
        val hisStatus : TextView = itemView.findViewById(R.id.tvHistoryStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = historyList[position]
        holder.hisImage.setImageResource(R.drawable.bg)
        holder.hisId.text = coffee.id
        holder.hisName.text = coffee.name
        holder.hisTotal.text = "Rp: ${coffee.total}"
        holder.hisStatus.text = coffee.status
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
