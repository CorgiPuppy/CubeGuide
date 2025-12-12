package com.example.cubeguide

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class ShopFragment : Fragment() {
    private lateinit var viewModel: ShopViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav:BottomNavigationView
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

        recyclerView = view.findViewById(R.id.rv_products)
        bottomNav = view.findViewById(R.id.bottom_nav_view)

        adapter = ProductAdapter(viewModel.products) { product ->
            viewModel.selectedProduct = product
            findNavController().navigate(R.id.action_shopFragment_to_productDetailsFragment)
        }
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter.updateData(viewModel.products)
                    true
                }
                R.id.nav_favourites -> {
                    recyclerView.layoutManager = GridLayoutManager(context, 2)
                    adapter.updateData(viewModel.favList)
                    true
                }
                R.id.nav_cart -> {
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter.updateData(viewModel.cartList)
                    true
                }
                else -> false
            }
        }

        viewModel.cartCount.observe(viewLifecycleOwner) { count ->
            if (count >0) {
                val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
                badge.number = count
                badge.isVisible = true
            }
        }

        viewModel.favCount.observe(viewLifecycleOwner) { count ->
            if (count > 0) {
                val badge = bottomNav.getOrCreateBadge(R.id.nav_favourites)
                badge.number = count
                badge.isVisible = true
            }
        }

        return view
    }
}