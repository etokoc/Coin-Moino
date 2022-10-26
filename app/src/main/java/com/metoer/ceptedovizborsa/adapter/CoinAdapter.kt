package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.data.response.coin.CoinData
import com.metoer.ceptedovizborsa.databinding.CoinBlockchainItemBinding
import java.text.DecimalFormat

class CoinAdapter(
    val items: List<CoinData>
) : RecyclerView.Adapter<CoinAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinBlockchainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CoinBlockchainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = items[position]
        holder.binding.apply {
            coinExchangeNameText.text = currentItem.name
            coinExchangeSembolText.text = currentItem.symbol
            val value = currentItem.priceUsd?.toDouble()
            coinExchangeValueText.text = "$"+DecimalFormat("##.######").format(value)
            val parcent = currentItem.changePercent24Hr?.toDouble()
            coinExchangeParcentText.text = DecimalFormat("##.##").format(parcent) + "%"
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}