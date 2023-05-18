package com.metoer.ceptedovizborsa.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.trades.CoinTradeData
import com.metoer.ceptedovizborsa.databinding.CoinRatesItemBinding
import com.metoer.ceptedovizborsa.util.GlobalThemeUtil
import com.metoer.ceptedovizborsa.util.appliedTheme
import com.metoer.ceptedovizborsa.util.patternText
import com.metoer.ceptedovizborsa.util.textColors
import java.text.SimpleDateFormat
import java.util.*

class CoinTradeAdapter() : RecyclerView.Adapter<CoinTradeAdapter.ListViewHolder>() {

    private var _itemList = mutableListOf<CoinTradeData>()

    fun setData(itemList: CoinTradeData) {
        _itemList.add(itemList)
        if (_itemList.size == 20) {
            _itemList.removeFirst()
        }
        notifyDataSetChanged()
    }

    class ListViewHolder(val binding: CoinRatesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = CoinRatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (_itemList.size > 20) 20 else _itemList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = _itemList[position]
        holder.binding.apply {
            val sdf = SimpleDateFormat("HH:mm:ss")
            val time = sdf.format(currentItem.eventTime?.let { Date(it) })
            textViewTime.text = time
            textViewValue.patternText(currentItem.price, "###,###,###,###.########")
            textViewQuantity.patternText(currentItem.quantity, "###,###,###,###.######")
            if (currentItem.buyerMarketMaker == true) {
                textViewValue.appliedTheme(GlobalThemeUtil.getTheme(holder.binding.root.context).second)
            } else {
                textViewValue.appliedTheme(GlobalThemeUtil.getTheme(holder.binding.root.context).first)
            }
        }
    }
}