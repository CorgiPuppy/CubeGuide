package com.example.cubeguide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cubeguide.data.CartItem

class CartAdapter(
    private var items: List<CartItem>,
    private val onPlusClick: (CartItem) -> Unit,
    private val onMinusClick: (CartItem) -> Unit,
    private val onDeleteClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.iv_cart_image)
        val name: TextView = view.findViewById(R.id.tv_cart_name)
        val price: TextView = view.findViewById(R.id.tv_cart_price)
        val quantity: TextView = view.findViewById(R.id.tv_quantity)
        val btnPlus: Button = view.findViewById(R.id.btn_plus)
        val btnMinus: Button = view.findViewById(R.id.btn_minus)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.price.text = "${item.price * item.quantity} ₽" // Общая цена позиции
        holder.quantity.text = item.quantity.toString()

        Glide.with(holder.itemView).load(item.imageUri).placeholder(R.drawable.logo).into(holder.img)

        holder.btnPlus.setOnClickListener { onPlusClick(item) }
        holder.btnMinus.setOnClickListener { onMinusClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}