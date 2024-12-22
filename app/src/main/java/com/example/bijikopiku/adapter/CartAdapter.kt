package com.example.bijikopiku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bijikopiku.R
import com.example.bijikopiku.helper.CartManager
import com.example.bijikopiku.model.dto.CartItem
import com.example.bijikopiku.model.response.Coffee

class CartAdapter(
    private val historyList: MutableList<Coffee>,
    private val cartManager: CartManager,
    private var total: Int,
    private val onTotalChanged: (Int) -> Unit,
    private val onEmptyHistory : () -> Unit
) :
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
        holder.amount.text = coffee.quantity.toString()

        holder.increment.setOnClickListener {
            onIncrementClick(position)
            val curPosition = historyList[position]
            val curItem = CartItem(curPosition.id, curPosition.quantity)
            cartManager.addToCartOrUpdateQuantity(curItem)
            total += coffee.price.toInt()
            onTotalChanged(total)
        }
        holder.decrement.setOnClickListener {
            if (coffee.quantity == 1) {
                total -= coffee.price.toInt()
                onTotalChanged(total)
                cartManager.removeFromCartOrUpdateQuantity(CartItem(coffee.id, coffee.quantity))
                historyList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, historyList.size - position)
                if (historyList.isEmpty()) {
                    onEmptyHistory()
                }
            } else {
                onDecrementClick(position)
                val updatedItem = CartItem(coffee.id, coffee.quantity)
                cartManager.removeFromCartOrUpdateQuantity(updatedItem)
                total -= coffee.price.toInt()
                onTotalChanged(total)
            }
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    private fun onIncrementClick(position: Int) {
        var coffee = historyList[position]
        val newAmount = coffee.quantity + 1
        coffee.quantity = newAmount
        notifyItemChanged(position)
    }

    private fun onDecrementClick(position: Int) {
        var coffee = historyList[position]
        val newAmount = coffee.quantity - 1
        coffee.quantity = newAmount
        notifyItemChanged(position)
    }
}
