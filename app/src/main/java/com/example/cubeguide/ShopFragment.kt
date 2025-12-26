package com.example.cubeguide

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cubeguide.data.Product
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        viewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]

        recyclerView = view.findViewById(R.id.rv_products)
        bottomNav = view.findViewById(R.id.bottom_nav_view)
        fabAdd = view.findViewById(R.id.fab_add)

        adapter = ProductAdapter(
            emptyList(),
            onClick = { product ->
                viewModel.selectedProduct = product
                findNavController().navigate(R.id.action_shopFragment_to_productDetailsFragment)
            },
            onLongClick = { product ->
                showDeleteDialog(product)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            adapter.updateData(products)
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addProductFragment)
        }

        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_favourites -> {
                    findNavController().navigate(R.id.action_shopFragment_to_favouritesFragment)
                    false
                }
                R.id.nav_cart -> {
                    findNavController().navigate(R.id.action_shopFragment_to_cartFragment)
                    false
                }
                else -> false
            }
        }

        setupBadges()

        return view
    }

    private fun setupBadges() {
        viewModel.cartItems.observe(viewLifecycleOwner) { list ->
            val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
            val totalCount = list.sumOf { it.quantity }

            if (totalCount > 0) {
                badge.isVisible = true
                badge.number = totalCount
            } else {
                badge.isVisible = false
            }
        }

        viewModel.favProducts.observe(viewLifecycleOwner) { list ->
            val badge = bottomNav.getOrCreateBadge(R.id.nav_favourites)
            if (list.isNotEmpty()) {
                badge.isVisible = true
                badge.number = list.size
            } else {
                badge.isVisible = false
            }
        }
    }

    private fun showDeleteDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить товар?")
            .setMessage("Вы точно хотите удалить ${product.name}?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteProductFull(product)
                Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}