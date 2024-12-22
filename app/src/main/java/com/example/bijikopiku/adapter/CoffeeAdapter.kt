package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bijikopiku.R
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CartItem
import com.example.bijikopiku.model.response.Coffee

class CoffeeAdapter(private val historyList: List<Coffee>, private val cartManager: CartManager) :
    RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeImage: ImageView = itemView.findViewById(R.id.ivCoffeeImage)
        val coffeeName: TextView = itemView.findViewById(R.id.tvCoffeeName)
        val coffeePrice: TextView = itemView.findViewById(R.id.tvCoffeePrice)
        val coffeeType : TextView = itemView.findViewById(R.id.tvCoffeeType)
        val addToCart: ImageView = itemView.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = historyList[position]
        holder.coffeeImage.setImageResource(R.drawable.bg)
        holder.coffeeName.text = coffee.name
        holder.coffeePrice.text = "Rp: ${coffee.price}"
        holder.coffeeType.text = coffee.type

        holder.addToCart.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Berhasil menambahkan ${coffee.name} ke keranjang", Toast.LENGTH_SHORT).show()
            val cartItem = CartItem(coffee.id, 1)
            cartManager.addToCartOrUpdateQuantity(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
