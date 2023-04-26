package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.databinding.CurrencyItemListBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import java.util.*

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: CurrencyItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<RatesData>) {
        setData(filterList)
    }

    // private var itemList = emptyList<Currency>()
    private var itemList = emptyList<RatesData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CurrencyItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItems = itemList[position]
        holder.binding.apply {
            moneyNameTextView.text = currentItems.id
            moneyCodeTextView.text = currentItems.symbol
            //val result = currentItems.ForexBuying?.div(currentItems.Unit!!)!!
            val result = currentItems.rateUsd!!.toDouble() * CurrencyViewModel.turkishValue
            moneyValueTextView.text = holder.itemView.context.getString(
                R.string.money_value,
                NumberDecimalFormat.numberDecimalFormat(result.toString(), "0.####")
            )
            if (currentItems.symbol == "XAU") {
                moneyImage.setImageResource(R.drawable.ingot)
            } else if (currentItems.symbol == "XAG") {
                moneyImage.setImageResource(R.drawable.silver)
            } else {
                Glide.with(this.root).load(
                    Constants.IMAGE_URL + "${
                        currentItems.symbol?.lowercase(
                            Locale.ENGLISH
                        )
                    }.png"
                ).diskCacheStrategy(DiskCacheStrategy.ALL).encodeQuality(50)
                    .format(DecodeFormat.PREFER_RGB_565).into(moneyImage)
            }
        }
    }

    fun setData(newItemList: List<RatesData>) {
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