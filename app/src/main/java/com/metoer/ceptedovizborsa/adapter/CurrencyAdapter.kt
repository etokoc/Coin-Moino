package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.CurrencyItemListBinding
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.ListSortEnum
import com.metoer.ceptedovizborsa.util.SortListUtil
import java.text.DecimalFormat
import java.util.*

class CurrencyAdapter :
    RecyclerView.Adapter<CurrencyAdapter.ListViewHolder>() {

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
            moneyNameTextView.text = currentItems.Isim
            moneyCodeTextView.text = currentItems.CurrencyCode
            val result = currentItems.ForexBuying?.div(currentItems.Unit!!)!!
            moneyValueTextView.text = "₺" + DecimalFormat("##.####").format(result)
            Glide.with(this.root).load(
                Constants.IMAGE_URL + "${
                    currentItems.CurrencyCode?.lowercase(
                        Locale.ENGLISH
                    )
                }.png"
            )
                .into(moneyImage)
        }
    }

    fun setData(newItemList: List<Currency>) {
        val diffUtil = com.metoer.ceptedovizborsa.util.DiffUtil(itemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, itemList.size)
    }

    fun sortList(listSortEnum: ListSortEnum) {
        when (listSortEnum) {
            ListSortEnum.ASC -> {
            }
            ListSortEnum.DESC -> {

            }
        }
//       val sortedList = itemList.sortedBy { it.Isim }
        val list = SortListUtil(
            itemList,
            ListSortEnum.ASC,
            ListSortEnum.NAME)
        setData(
            list.sortedList()
        )
    }

    override fun getItemCount(): Int {
        //XDR para birimini almamak için yaptık
        return itemList.size
    }
}