package com.example.cubeguide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cubeguide.data.AppDatabase
import com.example.cubeguide.data.CartItem
import com.example.cubeguide.data.Category
import com.example.cubeguide.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).shopDao()

    val allProducts: LiveData<List<Product>> = dao.getAllProducts()
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()
    val favProducts: LiveData<List<Product>> = dao.getFavouriteProducts()
    val cartItems: LiveData<List<CartItem>> = dao.getCartItems()

    var selectedProduct: Product? = null

    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingItem = dao.getCartItemByName(product.name)

            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                dao.updateCartItem(updatedItem)
            } else {
                val newItem = CartItem(
                    name = product.name,
                    price = product.price,
                    imageUri = product.imageUri,
                    quantity = 1
                )
                dao.insertCartItem(newItem)
            }
        }
    }

    fun updateCartItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedItem = item.copy(quantity = newQuantity)
            dao.updateCartItem(updatedItem)
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCartItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearCart()
        }
    }

    fun toggleFavourite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedProduct = product.copy(isFavourite = !product.isFavourite)
            dao.updateProduct(updatedProduct)
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCategory(Category(name = name))
        }
    }

    fun addProductToDb(name: String, desc: String, price: Int, imgUri: String, catId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newProduct = Product(
                name = name,
                description = desc,
                price = price,
                imageUri = imgUri,
                categoryId = catId,
                isFavourite = false
            )
            dao.insertProduct(newProduct)
        }
    }

    fun deleteProductFull(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteProduct(product)

            val cartItem = dao.getCartItemByName(product.name)
            if (cartItem != null) {
                dao.deleteCartItem(cartItem)
            }
        }
    }
}