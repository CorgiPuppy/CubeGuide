package com.example.cubeguide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cubeguide.data.AppDatabase
import com.example.cubeguide.data.CartItem
import com.example.cubeguide.data.Category
import com.example.cubeguide.data.Product
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).shopDao()

    val allProducts: LiveData<List<Product>> = dao.getAllProducts()
    val favProducts: LiveData<List<Product>> = dao.getFavoriteProducts() // Только избранные

    // Для корзины просто берем размер списка, чтобы показать на бейджике
    val cartItems: LiveData<List<CartItem>> = dao.getCartItems()

    var selectedProduct: Product? = null

    // Методы добавления товара и категории (из лаб 4) остаются...
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()
    fun addCategory(name: String) { viewModelScope.launch { dao.insertCategory(Category(name = name)) } }
    fun addProductToDb(name: String, desc: String, price: Int, imageUri: String, catId: Int) {
        viewModelScope.launch {
            dao.insertProduct(Product(name = name, description = desc, price = price, imageUri = imageUri, categoryId = catId))
        }
    }

    // Добавление в корзину (сохраняем в БД)
    fun addToCart() {
        selectedProduct?.let { product ->
            // Получаем текущий список товаров в корзине (синхронно из LiveData value может быть null,
            // но для простой лабы сойдет, либо делаем проверку внутри корутины)
            val currentCart = cartItems.value ?: emptyList()

            // Ищем, есть ли уже такой товар в корзине (по имени)
            val existingItem = currentCart.find { it.name == product.name }

            viewModelScope.launch {
                if (existingItem != null) {
                    // Если есть - увеличиваем количество
                    existingItem.quantity += 1
                    dao.updateCartItem(existingItem)
                } else {
                    // Если нет - создаем новый
                    val newItem = CartItem(
                        name = product.name,
                        price = product.price,
                        imageUri = product.imageUri
                    )
                    dao.insertCartItem(newItem)
                }
            }
        }
    }

    // ИСПРАВЛЕНО: Удаление/Добавление в избранное
    fun toggleFavorite() {
        selectedProduct?.let { product ->
            viewModelScope.launch {
                // Меняем статус на противоположный
                product.isFavorite = !product.isFavorite
                // Обновляем в базе данных
                dao.updateProduct(product)
            }
        }
    }

    fun deleteProductFull(product: Product) {
        viewModelScope.launch {
            // 1. Удаляем из таблицы товаров (исчезнет из Магазина и Избранного)
            dao.deleteProduct(product)
            // 2. Удаляем из таблицы корзины (чтобы не висел там)
            dao.deleteFromCartByName(product.name)
        }
    }
}