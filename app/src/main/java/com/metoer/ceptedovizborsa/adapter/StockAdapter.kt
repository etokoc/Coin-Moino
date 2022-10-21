package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.stock.detail.HisseYuzeysel
import kotlinx.android.synthetic.main.stock_exchange_item.view.*

class StockAdapter(
    var items: List<HisseYuzeysel>
) : RecyclerView.Adapter<StockAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_exchange_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItems = items[position]
        holder.itemView.apply {
            stockExchangeSembolText.text = currentItems.sembol
            stockExchangeNameText.text = currentItems.aciklama
            stockExchangeValueText.text = currentItems.satis.toString()
            stockExchangeParcentText.text = currentItems.yuzdedegisim.toString()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}