package com.metoer.ceptedovizborsa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.databinding.CoinBlockchainItemBinding
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
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
            coinVolumeExchangeText.text =
                MoneyCalculateUtil.volumeShortConverter(currentItem.volumeUsd24Hr!!.toDouble())
            val value = currentItem.priceUsd?.toDouble()
            coinExchangeValueText.text = "$" + DecimalFormat("##.######").format(value)
            val parcent = currentItem.changePercent24Hr?.toDouble()
            parcentBacgroundTint(parcent!!,coinExchangeParcentText,holder.itemView.context)
            coinExchangeParcentText.text = DecimalFormat("##.##").format(parcent) + "%"
        }
    }
    private fun parcentBacgroundTint(parcent:Double,textView: TextView,context: Context){
        if (parcent > 0) {
            textView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.coinValueRise
                )
            )
        } else if (parcent < 0) {
            textView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.coinValueDrop
                )
            )
        } else {
            textView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.appGray
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}