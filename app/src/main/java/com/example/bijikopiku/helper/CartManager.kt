package com.example.bijikopiku.helper

import android.content.Context
import android.util.Log
import com.example.bijikopiku.model.dto.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun saveCart(carts: List<CartItem>) {
        val json = gson.toJson(carts)
        sharedPreferences.edit().putString("carts", json).apply()
    }

    fun replaceCart(carts: List<CartItem>) {
        val json = gson.toJson(carts)
        sharedPreferences.edit().putString("carts", json).apply()
    }

    fun getCart(): List<CartItem> {
        val json = sharedPreferences.getString("carts", null)
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addToCartOrUpdateQuantity(item: CartItem) {
        val currentCart = getCart()
        val updatedCart = currentCart.toMutableList()
        val index = updatedCart.indexOfFirst { it.id == item.id }
        if (index != -1) {
            Log.d("CartManager", "Item already exists in cart, updating quantity")
            val existingItem = updatedCart[index]
            updatedCart[index] = existingItem.copy(quantity = item.quantity)
        } else {
            Log.d("CartManager", "Item does not exist in cart, adding new item")
            updatedCart.add(item)
        }
        saveCart(updatedCart)
    }

    fun removeFromCartOrUpdateQuantity(item: CartItem) {
        val currentCart = getCart()
        val updatedCart = currentCart.toMutableList()
        val index = updatedCart.indexOfFirst { it.id == item.id }
        if (index != -1) {
            Log.d("CartManager", "Item already exists in cart, updating quantity")
            val existingItem = updatedCart[index]
            if (existingItem.quantity > 1) {
                updatedCart[index] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                updatedCart.removeAt(index)
            }
        }
        saveCart(updatedCart)
    }

    fun findItemById(id: String): CartItem? {
        val currentCart = getCart()
        return currentCart.find { it.id == id }
    }

    fun clearCart() {
        sharedPreferences.edit().remove("carts").apply()
    }

}