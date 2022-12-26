package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.CurrencyItemListBinding
import com.metoer.ceptedovizborsa.util.*
import java.util.*

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: CurrencyItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<Currency>) {
        setData(filterList)
    }

    private var itemList = emptyList<Currency>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CurrencyItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItems = itemList[position]
        holder.binding.apply {
            if (currentItems.Kod!!.trim().lowercase() != "try") {
                val resId: Int = holder.itemView.resources.getIdentifier(
                    currentItems.Kod!!.trim().lowercase(),
                    "string", holder.itemView.context.packageName
                )
                moneyNameTextView.text = holder.itemView.context.getString(resId)
            } else {
                moneyNameTextView.text = holder.itemView.context.getString(R.string.tr)
            }
            moneyCodeTextView.text = currentItems.CurrencyCode
            val result = currentItems.ForexBuying?.div(currentItems.Unit!!)!!
            moneyValueTextView.text = holder.itemView.context.getString(
                R.string.money_value,
                NumberDecimalFormat.numberDecimalFormat(result.toString(), "0.####")
            )
            Glide.with(this.root).load(
                Constants.IMAGE_URL + "${
                    currentItems.CurrencyCode?.lowercase(
                        Locale.ENGLISH
                    )
                }.png"
            ).into(moneyImage)
        }
    }

    fun setData(newItemList: List<Currency>) {
        val diffUtil = DiffUtil(itemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, itemList.size)
    }

    fun sortList(listSortType: ListSortEnum, listSortItem: ListSortEnum) {
        val newList = SortListUtil(
        )
        setData(
            newList.sortedForCurrencyList(
                itemList, listSortType, listSortItem
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}