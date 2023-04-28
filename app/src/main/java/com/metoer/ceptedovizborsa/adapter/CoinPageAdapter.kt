package com.metoer.ceptedovizborsa.adapter

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinWebSocketResponse
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem
import com.metoer.ceptedovizborsa.databinding.CoinMarketsblockchainItemBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.view.activity.ChartActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class CoinPageAdapter(
    val coinPageTickerTypeEnum: PageTickerTypeEnum
) : RecyclerView.Adapter<CoinPageAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinMarketsblockchainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<CoinPageTickerItem>) {
        setData(filterList as ArrayList<CoinPageTickerItem>)
    }

    private var itemList = mutableListOf<CoinPageTickerItem>()
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

            val symbol = currentItem.symbol
            val lastPrice = currentItem.lastPrice
            val parcent = currentItem.priceChangePercent?.toDouble()

            if (symbol != null) {

                val leftOf: String = when {
                    symbol.endsWith("USDT") -> symbol.substring(
                        0,
                        symbol.length - 4
                    )
                    else -> symbol.substring(0, symbol.length - 3)
                }
                coinPriceChangeText.patternText(currentItem.priceChange, "###,###,###,###.########")
                coinExchangeSembolText.text = leftOf

                Glide.with(this.root).load(
                    "https://assets.coincap.io/assets/icons/" + "${
                        leftOf.lowercase(
                            Locale.ENGLISH
                        )
                    }@2x.png"
                ).diskCacheStrategy(DiskCacheStrategy.ALL).encodeQuality(50)
                    .format(DecodeFormat.PREFER_RGB_565).into(coinMarketImageView)

                coinQuoteSembolText.text = holder.itemView.context.getString(
                    R.string.quote_symbol,
                    coinPageTickerTypeEnum
                )
                if (parcent != null && parcent > 0) {
                    coinExchangeParcentText.background.setTint(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.coinValueRise
                        )
                    )
                } else if (parcent != null && parcent < 0) {
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
            /**
             * Buradaki kod textviewdaki virgüllü sayının decimale uygun olmaması yüzünden yapıldı
             * */
            val result: Double =
                MoneyCalculateUtil.doubleConverter(coinExchangeValueText.text.toString())
            if ((lastPrice?.toDouble() ?: 0.0) > result) {
                coinExchangeValueText.textColors(R.color.coinValueRise)
                Handler(Looper.getMainLooper()).postDelayed({
                    coinExchangeValueText.setTextAppearance(R.style.TextColor)
                }, 2000)
            } else if ((lastPrice?.toDouble() ?: 0.0) < result) {
                coinExchangeValueText.textColors(R.color.coinValueDrop)
                Handler(Looper.getMainLooper()).postDelayed({
                    coinExchangeValueText.setTextAppearance(R.style.TextColor)
                }, 2000)
            } else {
                coinExchangeValueText.setTextAppearance(R.style.TextColor)
            }
            coinVolumeExchangeText.text =
                currentItem.quoteVolume?.toDouble()
                    ?.let {
                        MoneyCalculateUtil.volumeShortConverter(
                            it,
                            holder.itemView.context
                        )
                    }
            coinExchangeValueText.patternText(lastPrice, "###,###,###,###.########")

        }
    }

    fun setData(newItemList: MutableList<CoinPageTickerItem>) {
        val diffUtil = DiffUtil(itemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        itemList = arrayListOf()
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, getListSize())
    }

    fun updateData(newData: CoinWebSocketResponse?, index: Int): MutableList<CoinPageTickerItem> {
        if (newData != null) {
            if (index < itemList.size) {
                itemList[index].lastPrice = newData.lastPrice.toString()
                itemList[index].priceChangePercent = newData.priceChangePercent.toString()
                itemList[index].quoteVolume = newData.queteVolume.toString()
                itemList[index].priceChange = newData.priceChange.toString()
                notifyItemChanged(index)
            }
        }
        return itemList
    }

    fun sortList(
        listSortType: FilterEnum,
        listSortItem: FilterEnum
    ): MutableList<CoinPageTickerItem> {
        if (!itemList.isEmpty()) {
            val newList = SortListUtil()
            setData(
                newList.sortedForCoinList(
                    itemList, listSortType, listSortItem
                ) as MutableList<CoinPageTickerItem>
            )
        }
        return itemList
    }

    private fun getListSize() = itemList.size

    override fun getItemCount(): Int {
        return getListSize()
    }
}