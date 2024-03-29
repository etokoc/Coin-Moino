package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.databinding.CoinPortfolioItemBinding
import com.metoer.ceptedovizborsa.util.DiffUtil
import com.metoer.ceptedovizborsa.util.NumberDecimalFormat
import com.metoer.ceptedovizborsa.util.onItemClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CoinPortfolioAdapter(
    val listener: onItemClickListener
) : RecyclerView.Adapter<CoinPortfolioAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CoinPortfolioItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var itemList = emptyList<CoinBuyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CoinPortfolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.apply {
            textViewCoinPortSymbol.text = currentItem.coinSymbol
            textViewCoinPortName.text = currentItem.coinName
            textViewCoinPortPrice.text = holder.itemView.context.getString(
                R.string.coin_portfolio_price_str,
                currentItem.coinSymbolQuote,
                currentItem.coinTakedValue
            )
            textViewCoinPortUnit.text = NumberDecimalFormat.numberDecimalFormat(
                currentItem.coinUnit.toString(),
                "###,###,###,###.######"
            )
            textViewCoinPortDate.text = getDate(currentItem.coinTakedTime!!)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, holder.binding.root)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter: DateFormat = SimpleDateFormat("MMM dd,yyyy  HH:mm", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun setData(newItemList: List<CoinBuyItem>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}