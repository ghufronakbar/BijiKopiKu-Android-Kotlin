package com.example.bijikopiku.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bijikopiku.R
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CartItem
import com.example.bijikopiku.model.response.Coffee
import com.example.bijikopiku.ui.DetailCoffeeActivity

class CoffeeAdapter(
    private val historyList: List<Coffee>,
    private val cartManager: CartManager,
    context: Context
) :
    RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeImage: ImageView = itemView.findViewById(R.id.ivCoffeeImage)
        val coffeeName: TextView = itemView.findViewById(R.id.tvCoffeeName)
        val coffeePrice: TextView = itemView.findViewById(R.id.tvCoffeePrice)
        val coffeeType: TextView = itemView.findViewById(R.id.tvCoffeeType)
        val addToCart: ImageView = itemView.findViewById(R.id.addToCart)
        val itemCoffee: CardView = itemView.findViewById(R.id.itemCoffee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = historyList[position]

        Glide.with(holder.itemView.context)
            .load(coffee.picture)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.coffeeImage)

        holder.coffeeName.text = coffee.name
        holder.coffeePrice.text = "Rp: ${coffee.price}"
        holder.coffeeType.text = coffee.type

        holder.itemCoffee.setOnClickListener {
            val intent = Intent()
            intent.setClass(holder.itemView.context, DetailCoffeeActivity::class.java)
            intent.putExtra("id", coffee.id)
            holder.itemView.context.startActivity(intent)
        }

        holder.addToCart.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Berhasil menambahkan ${coffee.name} ke keranjang",
                Toast.LENGTH_SHORT
            ).show()
            val cartItem = CartItem(coffee.id, 1)
            cartManager.addToCartOrUpdateQuantity(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
