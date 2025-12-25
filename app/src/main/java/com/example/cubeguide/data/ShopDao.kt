package com.example.cubeguide.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShopDao {
    // --- ТОВАРЫ ---
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    // Получить только избранные
    @Query("SELECT * FROM products WHERE isFavorite = 1")
    fun getFavoriteProducts(): LiveData<List<Product>>

    @Insert
    suspend fun insertCategory(category: Category)

    @Insert
    suspend fun insertProduct(product: Product)

    // Обновление товара (например, лайка)
    @Update
    suspend fun updateProduct(product: Product)

    // --- КОРЗИНА ---
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): LiveData<List<CartItem>>

    @Insert
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Delete
    suspend fun deleteProduct(product: Product)

    // Удаляет товар из корзины по имени (чтобы почистить везде)
    @Query("DELETE FROM cart_items WHERE name = :productName")
    suspend fun deleteFromCartByName(productName: String)
}