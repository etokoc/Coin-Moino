package com.metoer.ceptedovizborsa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.CoinMarketsblockchainItemBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.view.activity.ChartActivity


class CoinPageAdapter(
    val baseId: String
) : RecyclerView.Adapter<CoinPageAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinMarketsblockchainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<MarketData>) {
        setData(filterList)
    }

    private var itemList = emptyList<MarketData>()
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
        val currentItem = itemList[position]
        holder.binding.apply {
            if (currentItem.priceQuote.toDouble() != 0.0) {
                coinExchangeNameText.text = currentItem.baseId.uppercase()
                coinExchangeSembolText.text = currentItem.baseSymbol
                coinQuoteSembolText.text = holder.itemView.context.getString(
                    R.string.quote_symbol,
                    currentItem.quoteSymbol
                )
                coinVolumeExchangeText.text =
                    MoneyCalculateUtil.volumeShortConverter(currentItem.volumeUsd24Hr.toDouble(),holder.itemView.context)
                val value = currentItem.priceQuote
                coinExchangeValueText.text =
                    NumberDecimalFormat.numberDecimalFormat(value, "###,###,###,###.######")
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
                coinExchangeParcentText.text =
                    holder.itemView.context.getString(
                        R.string.coin_exchange_parcent_text,
                        NumberDecimalFormat.numberDecimalFormat(parcent.toString(), "0.##"), "%"
                    )
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

    private val coinList = StaticCoinList.coinList
    private fun caltulateMainCoin(baseSymbol: String): Double {
        val usdt = coinList.filter { x -> x.symbol == baseId }.first()
        val otherCoin = coinList.filter { x -> x.symbol == baseSymbol }.firstOrNull() ?: CoinData(
            "0.0",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        return (otherCoin.changePercent24Hr!!.toDouble()) - (usdt.changePercent24Hr!!.toDouble())
    }

    fun setData(newItemList: List<MarketData>) {
        val diffUtil = DiffUtil(itemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        itemList = listOf()
        newItemList.forEach {
            it.percentExchangeVolume = caltulateMainCoin(it.baseSymbol).toString()
        }
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, getListSize())
    }

    fun sortList(listSortType: FilterEnum, listSortItem: FilterEnum) {
        val newList = SortListUtil()
        setData(
            newList.sortedForCoinList(
                getFilteredList(), listSortType, listSortItem
            )
        )
    }

    private fun getFilteredList() = itemList.filter { it.priceQuote.toDouble() != 0.0 }
    private fun getListSize() = getFilteredList().size

    override fun getItemCount(): Int {
        return getListSize()
    }
}