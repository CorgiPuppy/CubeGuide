package com.example.cubeguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.cubeguide.data.Product // Используем новый Product из data

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var adapter: ProductAdapter
    // Эту переменную мы обязательно должны найти в макете
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

        // 1. Инициализируем ВСЕ View СРАЗУ
        recyclerView = view.findViewById(R.id.rv_products)
        bottomNav = view.findViewById(R.id.bottom_nav_view)
        fabAdd = view.findViewById(R.id.fab_add) // <--- ВОТ ЭТО ВАЖНО

        // Инициализируем адаптер пустым списком
        // Передаем ДВА действия: обычный клик и долгий клик (удаление)
        adapter = ProductAdapter(
            productList = emptyList(),
            onClick = { product ->
                viewModel.selectedProduct = product
                findNavController().navigate(R.id.action_shopFragment_to_productDetailsFragment)
            },
            onLongClick = { product ->
                showDeleteDialog(product)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // НАБЛЮДАЕМ ЗА ДАННЫМИ ИЗ БД
        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            // Обновляем список, только если мы на вкладке "Главная"
            if (bottomNav.selectedItemId == R.id.nav_home) {
                adapter.updateData(products)
            }
        }

        // Наблюдаем за Избранным (для бейджа и списка)
        viewModel.favProducts.observe(viewLifecycleOwner) { favs ->
            if (bottomNav.selectedItemId == R.id.nav_favourites) {
                adapter.updateData(favs)
            }
            val badge = bottomNav.getOrCreateBadge(R.id.nav_favourites)
            badge.isVisible = favs.isNotEmpty()
            badge.number = favs.size
        }

        // Обновляем бейдж корзины
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
            badge.isVisible = cartItems.isNotEmpty()
            badge.number = cartItems.size
        }

        // Переход на экран добавления
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addProductFragment)
        }

        // Логика переключения вкладок
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    // Показываем ВСЕ товары
                    viewModel.allProducts.value?.let { adapter.updateData(it) }
                    fabAdd.show() // Показываем кнопку добавления
                    true
                }
                R.id.nav_favourites -> {
                    recyclerView.layoutManager = GridLayoutManager(context, 2)
                    // Показываем ИЗБРАННОЕ
                    viewModel.favProducts.value?.let { adapter.updateData(it) }
                    fabAdd.hide() // Скрываем кнопку добавления
                    true
                }
                R.id.nav_cart -> {
                    // Переход в корзину
                    findNavController().navigate(R.id.action_shopFragment_to_cartFragment)
                    false
                }
                else -> false
            }
        }

        // Проверяем начальное состояние кнопки
        if (bottomNav.selectedItemId == R.id.nav_home) {
            fabAdd.show()
        } else {
            fabAdd.hide()
        }

        return view
    }

    // Диалог удаления
    private fun showDeleteDialog(product: Product) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Удаление товара")
            .setMessage("Вы точно хотите удалить \"${product.name}\"?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteProductFull(product)
                android.widget.Toast.makeText(context, "Товар удален", android.widget.Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}