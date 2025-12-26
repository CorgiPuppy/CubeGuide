package com.example.cubeguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
        recyclerView = view.findViewById(R.id.rv_favourites)

        val adapter = ProductAdapter(
            emptyList(),
            onClick = { product ->
                viewModel.selectedProduct = product
                findNavController().navigate(R.id.action_favouritesFragment_to_productDetailsFragment)
            },
            onLongClick = {}
        )

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter

        viewModel.favProducts.observe(viewLifecycleOwner) { products ->
            adapter.updateData(products)
        }

        return view
    }
}