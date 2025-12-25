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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.cubeguide.data.Product

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
        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

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
                if (bottomNav.selectedItemId == R.id.nav_home) {
                    showDeleteDialog(product)
                }
            }
        )

        recyclerView.adapter = adapter

        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            if (bottomNav.selectedItemId == R.id.nav_home) adapter.updateData(products)
        }
        viewModel.cartCount.observe(viewLifecycleOwner) { count ->
            val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
            badge.isVisible = count > 0
            badge.number = count
            if (bottomNav.selectedItemId == R.id.nav_cart) adapter.updateData(viewModel.cartList)
        }
        viewModel.favCount.observe(viewLifecycleOwner) { count ->
            val badge = bottomNav.getOrCreateBadge(R.id.nav_favourites)
            badge.isVisible = count > 0
            badge.number = count
            if (bottomNav.selectedItemId == R.id.nav_favourites) adapter.updateData(viewModel.favList)
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addProductFragment)
        }

        bottomNav.setOnItemSelectedListener { item ->
            updateUIForTab(item.itemId)
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomNav.selectedItemId != R.id.nav_home) {
                    bottomNav.selectedItemId = R.id.nav_home
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUIForTab(bottomNav.selectedItemId)
    }

    private fun updateUIForTab(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                viewModel.allProducts.value?.let { adapter.updateData(it) }
                fabAdd.show()
            }
            R.id.nav_favourites -> {
                recyclerView.layoutManager = GridLayoutManager(context, 2)
                adapter.updateData(viewModel.favList)
                fabAdd.hide()
            }
            R.id.nav_cart -> {
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter.updateData(viewModel.cartList)
                fabAdd.hide()
            }
        }
    }

    private fun showDeleteDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить товар?")
            .setMessage("Товар \"${product.name}\" будет удален отовсюду.")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteProductFull(product)
                Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}