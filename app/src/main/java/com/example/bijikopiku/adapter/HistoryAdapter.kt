package com.example.bijikopiku.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bijikopiku.R
import com.example.bijikopiku.model.response.Order
import com.example.bijikopiku.ui.DetailCoffeeActivity
import com.example.bijikopiku.ui.DetailOrderActivity

class HistoryAdapter(private val historyList: List<Order>) :
    RecyclerView.Adapter<HistoryAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hisId: TextView = itemView.findViewById(R.id.tvHistoryId)
        val hisImage: ImageView = itemView.findViewById(R.id.ivHistoryImage)
        val hisName: TextView = itemView.findViewById(R.id.tvHistoryName)
        val hisTotal: TextView = itemView.findViewById(R.id.tvHistoryTotal)
        val hisStatus: TextView = itemView.findViewById(R.id.tvHistoryStatus)
        val badgeStatus: CardView = itemView.findViewById(R.id.badgeStatus)
        val itemHistory: CardView = itemView.findViewById(R.id.itemHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val order = historyList[position]
        var name = ""
        if (order.orderItems.size == 1) {
            name += order.orderItems[0].coffee.name
        } else {
            name = "${order.orderItems[0].coffee.name}, dan ${order.orderItems.size - 1} lainnya"
        }

        when (order.status) {
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

        Glide.with(holder.itemView.context)
            .load(order.orderItems[0].coffee.picture)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.hisImage)

        holder.hisId.text = order.id
        holder.hisName.text = name
        holder.hisTotal.text = "Rp: ${order.total}"
        holder.hisStatus.text = order.status

        holder.itemHistory.setOnClickListener {
            val intent = Intent()
            intent.setClass(holder.itemView.context, DetailOrderActivity::class.java)
            intent.putExtra("id", order.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
