package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bijikopiku.R
import com.example.bijikopiku.model.response.History
import com.example.bijikopiku.model.response.Order

class HistoryAdapter(private val historyList: List<Order>) :
    RecyclerView.Adapter<HistoryAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hisId: TextView = itemView.findViewById(R.id.tvHistoryId)
        val hisImage: ImageView = itemView.findViewById(R.id.ivHistoryImage)
        val hisName: TextView = itemView.findViewById(R.id.tvHistoryName)
        val hisTotal: TextView = itemView.findViewById(R.id.tvHistoryTotal)
        val hisStatus: TextView = itemView.findViewById(R.id.tvHistoryStatus)
        val badgeStatus:CardView = itemView.findViewById(R.id.badgeStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = historyList[position]
        var name = ""
        for (i in 0 until coffee.orderItems.size) {
            name += coffee.orderItems[i].coffee.name + ", "
        }

        name = name.substring(0, name.length - 2)

        when (coffee.status) {
            "Dipesan" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.gray))
            }
            "Dibayar" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.yellow))
            }
            "Dibatalkan" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.orange))
            }
            "Ditolak" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.red))
            }
            "Diterima" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.green))
            }
            "Selesai" -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.primary))
            }
            else -> {
                holder.badgeStatus.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.gray))
            }
        }



        holder.hisImage.setImageResource(R.drawable.bg)
        holder.hisId.text = coffee.id
        holder.hisName.text = name
        holder.hisTotal.text = "Rp: ${coffee.total}"
        holder.hisStatus.text = coffee.status
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
