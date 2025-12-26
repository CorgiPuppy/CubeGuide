package com.example.cubeguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class ProductDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
        val product = viewModel.selectedProduct

        if (product != null) {
            view.findViewById<TextView>(R.id.tv_detail_title).text = product.name
            view.findViewById<TextView>(R.id.tv_detail_desc).text = product.description
            view.findViewById<TextView>(R.id.tv_detail_price).text = "${product.price} ₽"

            val img = view.findViewById<ImageView>(R.id.iv_detail_image)
            Glide.with(this).load(product.imageUri).placeholder(R.drawable.logo).into(img)

            val btnFav = view.findViewById<Button>(R.id.btn_detail_fav)
            btnFav.text = if (product.isFavourite) "Убрать из избранного" else "В избранное"

            btnFav.setOnClickListener {
                viewModel.toggleFavourite(product)
                btnFav.text = if (!product.isFavourite) "Убрать из избранного" else "В избранное"
                Toast.makeText(context, "Статус избранного изменен", Toast.LENGTH_SHORT).show()
            }

            view.findViewById<Button>(R.id.btn_detail_cart).setOnClickListener {
                viewModel.addToCart(product)
                Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}