package com.example.cubeguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ShopViewModel : ViewModel() {
    val products = listOf(
        Product(
            1,
            "Кубик 3x3",
            "Классический кубик Рубика для новичков.",
            500,
            R.drawable.logo
        ),
        Product(2,
            "Мегаминкс",
            "Головоломка с 12 гранями. Очень красивая.",
            1200,
            R.drawable.megaminx
        ),
        Product(3,
            "Кубик 4x4",
            "Месть Рубика. Сложнее, чем 3x3.",
            800,
            R.drawable.four_on_four
        ),
        Product(4,
            "Зеркальный",
            "Меняет форму при вращении. Выглядит эффектно.",
            600,
            R.drawable.mirror
        ),
        Product(5,
            "Скьюб",
            "Вращается по углам. Необычная механика.",
            550,
            R.drawable.squire_one
        ),
        Product(6,
            "Кубик 2x2",
            "Карманный кубик. Собирается быстро.",
            300,
            R.drawable.two_on_two
        ),
        Product(7,
            "Пирамидка",
            "Пираминкс. Тетраэдр для спидкубинга.",
            450,
            R.drawable.megaminx
        ),
        Product(8,
            "Мастерморфикс",
            "Сложная форма, меняет геометрию.",
            850,
            R.drawable.mirror
        ),
        Product(9,
            "Кубик 5x5",
            "Профессорский кубик для долгих вечеров.",
            1500,
            R.drawable.logo
        ),
        Product(10,
            "Скваер-1",
            "Меняет форму, сложные алгоритмы.",
            900,
            R.drawable.squire_one
        ),
        Product(11,
            "Смазка для куба",
            "Делает вращение плавным и быстрым.",
            250,
            R.drawable.logo
        ),
        Product(12,
            "Таймер",
            "Для замера скорости сборки.",
            1000,
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