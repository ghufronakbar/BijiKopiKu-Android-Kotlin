package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bijikopiku.R
import com.example.bijikopiku.model.response.OrderItem

class DetailHistoryAdapter(private val orderItems: List<OrderItem>) :
    RecyclerView.Adapter<DetailHistoryAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hisImage: ImageView = itemView.findViewById(R.id.ivDetailImage)
        val hisName: TextView = itemView.findViewById(R.id.tvItemName)
        val hisTotal: TextView = itemView.findViewById(R.id.tvItemTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_history, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val orderItem = orderItems[position]

        Glide.with(holder.itemView.context)
            .load(orderItem.coffee.picture)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.hisImage)

        holder.hisName.text = "${orderItem.coffee.name} x${orderItem.quantity}"
        holder.hisTotal.text = "Rp ${orderItem.total}"
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }
}
