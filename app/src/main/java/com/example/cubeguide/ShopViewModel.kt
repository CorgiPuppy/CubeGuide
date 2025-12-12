package com.example.cubeguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ShopViewModel : ViewModel() {
    val products = listOf(
        Product(
            1,
            "Кубик 3х3",
            "Классический кубик Рубика, подходит для новичков.",
            500,
            R.drawable.logo
        )
    )

    val cartList = mutableListOf<Product>()
    val favList = mutableListOf<Product>()

    val cartCount = MutableLiveData(0)
    val favCount = MutableLiveData(0)

    var selectedProduct: Product? = null

    fun addToCart() {
        if (selectedProduct != null && !cartList.contains(selectedProduct)) {
            cartList.add(selectedProduct!!)
            cartCount.value = cartList.size
        }
    }

    fun addToFav() {
        if (selectedProduct != null && !favList.contains(selectedProduct)) {
            favList.add(selectedProduct!!)
            favCount.value = favList.size
        }
    }
}