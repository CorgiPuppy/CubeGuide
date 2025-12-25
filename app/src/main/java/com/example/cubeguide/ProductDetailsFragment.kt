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
        val btnFav = view.findViewById<Button>(R.id.btn_detail_fav)

        if (product != null) {
            val imageView = view.findViewById<ImageView>(R.id.iv_detail_image)
            Glide.with(this)
                .load(product.imageUri)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView)

            view.findViewById<TextView>(R.id.tv_detail_title).text = product.name
            view.findViewById<TextView>(R.id.tv_detail_desc).text = product.description
            view.findViewById<TextView>(R.id.tv_detail_price).text = "${product.price} ₽"

            // Установка правильного текста кнопки при открытии
            updateFavButton(btnFav, viewModel.isProductFav(product))
        }

        view.findViewById<Button>(R.id.btn_detail_cart).setOnClickListener {
            viewModel.addToCart()
            Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_SHORT).show()
        }

        btnFav.setOnClickListener {
            viewModel.toggleFav()
            product?.let {
                val isFav = viewModel.isProductFav(it)
                updateFavButton(btnFav, isFav)
                val msg = if (isFav) "Добавлено в избранное" else "Удалено из избранного"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateFavButton(btn: Button, isFav: Boolean) {
        if (isFav) {
            btn.text = "Удалить из избранного"
        } else {
            btn.text = "В избранное"
        }
    }
}