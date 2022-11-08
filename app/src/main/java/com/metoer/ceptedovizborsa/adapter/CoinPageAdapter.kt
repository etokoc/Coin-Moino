package com.metoer.ceptedovizborsa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.CoinMarketsblockchainItemBinding
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
import com.metoer.ceptedovizborsa.util.StaticCoinList
import com.metoer.ceptedovizborsa.view.activity.ChartActivity
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
                coinExchangeValueText.text = DecimalFormat("0.######").format(value)
                val parcent = caltulateMainCoin(currentItem.baseSymbol)
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
                } else {
                    coinExchangeParcentText.background.setTint(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.appGray
                        )
                    )
                }
                coinExchangeParcentText.text = DecimalFormat("0.##").format(parcent) + "%"
            }
            itemRow.setOnClickListener {
                it.apply {
                    val intent = Intent(it.context, ChartActivity::class.java)
                    intent.putExtra("send", currentItem)
                    context.startActivity(intent)
                }
            }
        }
    }

    val coinList = StaticCoinList.coinList
    private fun caltulateMainCoin(baseSymbol: String): Double {
        val usdt = coinList.filter { x -> x.symbol == "USDT" }.first()
        val otherCoin = coinList.filter { x-> x.symbol == baseSymbol }.firstOrNull()?: CoinData("0.0","","","","","","","","","","","")
        return (otherCoin.changePercent24Hr!!.toDouble()) - (usdt.changePercent24Hr!!.toDouble())
    }

    override fun getItemCount(): Int {
        return items.size
    }
}