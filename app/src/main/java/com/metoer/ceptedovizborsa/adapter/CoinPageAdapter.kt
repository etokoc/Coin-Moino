package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.CoinBlockchainItemBinding
import java.text.DecimalFormat

class CoinPageAdapter(
    val items: List<MarketData>
) : RecyclerView.Adapter<CoinPageAdapter.ListViewHolder>() {
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
            coinExchangeNameText.text = currentItem.baseId.uppercase()
            coinExchangeSembolText.text = currentItem.baseSymbol
            if (currentItem.volumeUsd24Hr!!.toDouble() > 1000000000) {
                coinVolumeExchangeText.text="Hacim "+DecimalFormat("##.##").format(currentItem.volumeUsd24Hr!!.toDouble() / 1000000000)+" milyar"
            }
            else {
                coinVolumeExchangeText.text="Hacim "+DecimalFormat("##.##").format(currentItem.volumeUsd24Hr!!.toDouble() / 10000000)+" milyon"
            }
            val value = currentItem.priceUsd?.toDouble()
            coinExchangeValueText.text = "$" + DecimalFormat("##.######").format(value)
            val parcent = currentItem.percentExchangeVolume.toDouble()
            if (parcent > 0) {
                coinExchangeParcentText.setBackground(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.coin_value_rise_background
                    )
                );
            } else {
                coinExchangeParcentText.setBackground(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.coin_value_drop_background
                    )
                );
            }
            coinExchangeParcentText.text = DecimalFormat("##.##").format(parcent) + "%"
            //coinVolumeExchangeText.text = currentItem.volumeUsd24Hr
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}