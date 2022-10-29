package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.CoinMarketsblockchainItemBinding
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
import java.text.DecimalFormat


class CoinPageAdapter(
    val items: List<MarketData>
) : RecyclerView.Adapter<CoinPageAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinMarketsblockchainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CoinMarketsblockchainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = items[position]
        holder.binding.apply {
            if (currentItem.priceQuote.toDouble() != 0.0) {
                coinExchangeNameText.text = currentItem.baseId.uppercase()
                coinExchangeSembolText.text = currentItem.baseSymbol
                coinQuoteSembolText.text = "/${currentItem.quoteSymbol}"
                coinVolumeExchangeText.text =
                    MoneyCalculateUtil.volumeShortConverter(currentItem.volumeUsd24Hr.toDouble())
                val value = currentItem.priceQuote.toDouble()
                coinExchangeValueText.text = DecimalFormat("##.######").format(value)
                val parcent = currentItem.percentExchangeVolume.toDouble()
                if (parcent > 0) {
                    coinExchangeParcentText.background.setTint(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.coinValueRise
                        )
                    )
                } else if (parcent < 0) {
                    coinExchangeParcentText.background.setTint(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.coinValueDrop
                        )
                    )
                }else {
                    coinExchangeParcentText.background.setTint(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.appGray
                        )
                    )
                }
                coinExchangeParcentText.text = DecimalFormat("##.##").format(parcent) + "%"
            }
            itemRow.setOnClickListener {
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}