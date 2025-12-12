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

    val cartCount = MutableLiveData(0)
    val favCount = MutableLiveData(0)

    var selectedProduct: Product? = null

    fun addToCart() {
        cartCount.value = (cartCount.value ?: 0) + 1
    }

    fun addToFav() {
        favCount.value = (favCount.value ?: 0) + 1
    }
}