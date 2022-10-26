package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.data.response.coin.CoinData
import com.metoer.ceptedovizborsa.databinding.CoinBlockchainItemBinding

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
            coinExchangeNameText.text =currentItem.name
            coinExchangeSembolText.text =currentItem.symbol
            coinExchangeValueText.text =currentItem.priceUsd
            coinExchangeParcentText.text =currentItem.changePercent24Hr
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}