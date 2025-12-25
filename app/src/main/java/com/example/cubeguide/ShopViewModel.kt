package com.example.cubeguide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cubeguide.data.AppDatabase
import com.example.cubeguide.data.Category
import com.example.cubeguide.data.Product
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).shopDao()

    val allProducts: LiveData<List<Product>> = dao.getAllProducts()
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()

    val cartList = mutableListOf<Product>()
    val favList = mutableListOf<Product>()
    val cartCount = MutableLiveData(0)
    val favCount = MutableLiveData(0)

    var selectedProduct: Product? = null

    fun addCategory(name: String) {
        viewModelScope.launch {
            dao.insertCategory(Category(name = name))
        }
    }

    fun addProductToDb(name: String, desc: String, price: Int, imageUri: String, catId: Int) {
        viewModelScope.launch {
            val newProduct = Product(
                name = name,
                description = desc,
                price = price,
                imageUri = imageUri,
                categoryId = catId
            )
            dao.insertProduct(newProduct)
        }
    }

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