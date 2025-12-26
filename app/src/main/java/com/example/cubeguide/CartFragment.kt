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

    private lateinit var viewModel: ShopViewModel
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

        val rvCart = view.findViewById<RecyclerView>(R.id.rv_cart)
        val tvTotal = view.findViewById<TextView>(R.id.tv_total_sum)
        val btnCheckout = view.findViewById<Button>(R.id.btn_checkout)

        adapter = CartAdapter(
            emptyList(),
            onPlusClick = { item -> viewModel.updateCartItemQuantity(item, item.quantity + 1) },
            onMinusClick = { item ->
                if (item.quantity > 1) viewModel.updateCartItemQuantity(item, item.quantity - 1)
            },
            onDeleteClick = { item -> viewModel.removeFromCart(item) }
        )

        rvCart.layoutManager = LinearLayoutManager(context)
        rvCart.adapter = adapter

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
            val total = items.sumOf { it.price * it.quantity }
            tvTotal.text = "Итого: $total ₽"
        }

        btnCheckout.setOnClickListener {
            viewModel.clearCart()
            Toast.makeText(context, "Заказ оформлен!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}