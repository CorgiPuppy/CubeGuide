package com.example.cubeguide.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Int,
    val imageUri: String,
    var quantity: Int = 1
)