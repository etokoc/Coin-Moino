package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.CurrencyItemListBinding
import com.metoer.ceptedovizborsa.util.Constants
import kotlinx.android.synthetic.main.currency_item_list.view.*
import java.text.DecimalFormat
import java.util.*

class CurrencyAdapter :
    RecyclerView.Adapter<CurrencyAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: CurrencyItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterList(filterList: List<Currency>) {
        setData(filterList)
    }

    private var oldItemList = emptyList<Currency>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = CurrencyItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItems = oldItemList[position]
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
        val diffUtil = com.metoer.ceptedovizborsa.util.DiffUtil(oldItemList, newItemList)
        val diffResult = calculateDiff(diffUtil)
        oldItemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(oldItemList.size, oldItemList.size)
    }

    override fun getItemCount(): Int {
        //XDR para birimini almamak için yaptık
        return oldItemList.size
    }
}