package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class ProductDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)

        val viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
        val product = viewModel.selectedProduct

        if (product != null) {
            view.findViewById<ImageView>(R.id.iv_detail_image).setImageResource(product.imageResId)
            view.findViewById<TextView>(R.id.tv_detail_title).text = product.name
            view.findViewById<TextView>(R.id.tv_detail_desc).text = product.description
            view.findViewById<TextView>(R.id.tv_detail_price).text = "${product.price} ₽"
        }

        view.findViewById<Button>(R.id.btn_detail_cart).setOnClickListener {
            viewModel.addToCart()
        }

        view.findViewById<Button>(R.id.btn_detail_fav).setOnClickListener {
            viewModel.addToFav()
        }

        return view
    }
}