package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bijikopiku.R
import com.example.bijikopiku.model.CoffeeCart

class CartAdapter(private val historyList: List<CoffeeCart>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeImage: ImageView = itemView.findViewById(R.id.ivCoffeeImage)
        val coffeeName: TextView = itemView.findViewById(R.id.tvCoffeeName)
        val coffeePrice: TextView = itemView.findViewById(R.id.tvCoffeePrice)
        val coffeeType: TextView = itemView.findViewById(R.id.tvCoffeeType)
        val amount: TextView = itemView.findViewById(R.id.tvAmount)
        val increment: ImageView = itemView.findViewById(R.id.ivIncrement)
        val decrement: ImageView = itemView.findViewById(R.id.ivDecrement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val coffee = historyList[position]
        holder.coffeeImage.setImageResource(R.drawable.bg)
        holder.coffeeName.text = coffee.name
        holder.coffeePrice.text = "Rp: ${coffee.price}"
        holder.coffeeType.text = coffee.type
        holder.amount.text = coffee.amount.toString()

        holder.increment.setOnClickListener {
            onIncrementClick(position)
        }
        holder.decrement.setOnClickListener {
            onDecrementClick(position)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
    private fun onIncrementClick(position: Int) {
        var coffee = historyList[position]
        val newAmount = coffee.amount + 1
        coffee.amount = newAmount
        notifyItemChanged(position)
    }
    private fun onDecrementClick(position: Int) {
        var coffee = historyList[position]
        val newAmount = coffee.amount - 1
        coffee.amount = newAmount
        notifyItemChanged(position)
    }
}
