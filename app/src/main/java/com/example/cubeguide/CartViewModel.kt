package com.example.cubeguide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.cubeguide.data.AppDatabase
import com.example.cubeguide.data.CartItem
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).shopDao()

    // Список товаров в корзине (следит за БД)
    val cartItems: LiveData<List<CartItem>> = dao.getCartItems()

    // Автоматический подсчет общей суммы
    // Мы используем map, чтобы пересчитывать сумму каждый раз, когда меняется список
    val totalPrice: LiveData<Int> = cartItems.map { list ->
        list.sumOf { it.price * it.quantity }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            item.quantity += 1
            dao.updateCartItem(item)
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                item.quantity -= 1
                dao.updateCartItem(item)
            }
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            dao.deleteCartItem(item)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            dao.clearCart()
        }
    }
}