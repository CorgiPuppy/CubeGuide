package com.example.cubeguide.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM products WHERE isFavourite = 1")
    fun getFavouriteProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    // --- КОРЗИНА ---
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): LiveData<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE name = :name LIMIT 1")
    suspend fun getCartItemByName(name: String): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}