package com.example.cubeguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        val rvCart = view.findViewById<RecyclerView>(R.id.rv_cart)
        val tvTotal = view.findViewById<TextView>(R.id.tv_total_sum)
        val btnCheckout = view.findViewById<Button>(R.id.btn_checkout)

        val adapter = CartAdapter(
            emptyList(),
            onPlusClick = { viewModel.increaseQuantity(it) },
            onMinusClick = { viewModel.decreaseQuantity(it) },
            onDeleteClick = { viewModel.deleteItem(it) }
        )
        rvCart.adapter = adapter
        rvCart.layoutManager = LinearLayoutManager(context)

        // Наблюдаем за списком товаров
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
        }

        // Наблюдаем за общей суммой (автоматический пересчет)
        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            tvTotal.text = "Итого: ${total ?: 0} ₽"
        }

        btnCheckout.setOnClickListener {
            viewModel.checkout()
            Toast.makeText(context, "Заказ оформлен!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}