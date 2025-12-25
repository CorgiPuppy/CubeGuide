package com.example.cubeguide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cubeguide.data.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val onClick: (Product) -> Unit,
    private val onLongClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PRODUCT = 0
    private val TYPE_AD = 1

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_product)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val price: TextView = itemView.findViewById(R.id.tv_price)
    }

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // Реклама каждый 5-й элемент (индексы 4, 9, 14...)
    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 5 == 0) TYPE_AD else TYPE_PRODUCT
    }

    override fun getItemCount(): Int {
        if (productList.isEmpty()) return 0
        // Каждые 4 товара добавляют 1 слот рекламы
        val adsCount = productList.size / 4
        return productList.size + adsCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_AD) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ad, parent, false)
            AdViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            ProductViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PRODUCT) {
            val productHolder = holder as ProductViewHolder

            // Вычисляем реальный индекс товара, пропуская рекламу
            val adsBefore = (position + 1) / 5
            val realIndex = position - adsBefore

            // --- ЗАЩИТА ОТ ВЫЛЕТА ---
            if (realIndex >= 0 && realIndex < productList.size) {
                val product = productList[realIndex]

                productHolder.title.text = product.name
                productHolder.price.text = "${product.price} ₽"

                // Загрузка картинки
                Glide.with(holder.itemView.context)
                    .load(product.imageUri)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(productHolder.image)

                // Клики
                holder.itemView.setOnClickListener { onClick(product) }
                holder.itemView.setOnLongClickListener {
                    onLongClick(product)
                    true
                }
            }
        }
    }

    fun updateData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}