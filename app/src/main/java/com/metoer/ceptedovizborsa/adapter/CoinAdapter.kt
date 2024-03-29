package com.metoer.ceptedovizborsa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.databinding.CoinBlockchainItemBinding
import com.metoer.ceptedovizborsa.util.*
import java.util.*

class CoinAdapter(
    val listener: onItemClickListener
) : RecyclerView.Adapter<CoinAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinBlockchainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<CoinData>) {
        setData(filterList)
    }

    var itemList = emptyList<CoinData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CoinBlockchainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.apply {
            coinExchangeNameText.text = currentItem.name
            coinExchangeSembolText.text = currentItem.symbol
            coinVolumeExchangeText.text =
                MoneyCalculateUtil.volumeShortConverter(
                    currentItem.volumeUsd24Hr!!.toDouble(),
                    holder.itemView.context
                )
            Glide.with(this.root).load(
                "https://assets.coincap.io/assets/icons/" + "${
                    currentItem.symbol?.lowercase(
                        Locale.ENGLISH
                    )
                }@2x.png"
            ).into(coinImageView)
            val value = currentItem.priceUsd
            coinExchangeValueText.text =
                holder.itemView.context.getString(
                    R.string.coin_exchange_value_text,
                    NumberDecimalFormat.numberDecimalFormat(value!!, "###,###,###,###.######")
                )
            val parcent = currentItem.changePercent24Hr?.toDouble()
            parcent?.let {
                parcentBacgroundTint(it, coinExchangeParcentText, holder.itemView.context)
                coinExchangeParcentText.text = holder.itemView.context.getString(
                    R.string.coin_exchange_parcent_text,
                    NumberDecimalFormat.numberDecimalFormat(parcent.toString(), "0.##"), "%"
                )
            }
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, holder.binding.root)
        }
    }

    fun setData(newItemList: List<CoinData>) {
        val diffUtil = DiffUtil(itemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, itemList.size)

    }

    private fun parcentBacgroundTint(parcent: Double, textView: TextView, context: Context) {
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

    fun sortList(listSortType: FilterEnum, listSortItem: FilterEnum) {
        val newList = SortListUtil()
        setData(
            newList.sortedForCoinList(
                itemList, listSortType, listSortItem
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}