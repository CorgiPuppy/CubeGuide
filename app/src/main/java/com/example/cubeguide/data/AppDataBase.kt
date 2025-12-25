package com.example.cubeguide.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// ВАЖНО: меняем version на 2 и добавляем CartItem в entities
@Database(entities = [Category::class, Product::class, CartItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Описываем миграцию с версии 1 на 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Добавляем колонку isFavorite в таблицу products
                database.execSQL("ALTER TABLE products ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")

                // 2. Создаем новую таблицу cart_items
                database.execSQL("CREATE TABLE IF NOT EXISTS `cart_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `price` INTEGER NOT NULL, `imageUri` TEXT NOT NULL, `quantity` INTEGER NOT NULL)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop_db"
                )
                    .addMigrations(MIGRATION_1_2) // <-- Подключаем миграцию
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}