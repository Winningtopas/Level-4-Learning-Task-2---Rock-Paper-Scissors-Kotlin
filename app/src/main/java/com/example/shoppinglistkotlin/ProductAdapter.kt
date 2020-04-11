package com.example.shoppinglistkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.item_layout.view.*

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product) {
            itemView.ivHistoryComputerHand.setImageResource(product.computerHand)
            itemView.ivHistoryPlayerHand.setImageResource(product.playerHand)
            itemView.tvHistoryWinLose.text = product.winner
            itemView.tvHistoryDate.text = product.date.toString()



           // itemView.tvProduct.text = product.name
           // itemView.tvQuantity.text = product.quantity.toString()
        }
    }
}