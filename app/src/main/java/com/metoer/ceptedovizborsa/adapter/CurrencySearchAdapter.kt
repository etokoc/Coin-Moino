package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.databinding.CurrencyDialogItemBinding
import com.metoer.ceptedovizborsa.util.DiffUtil
import com.metoer.ceptedovizborsa.util.onItemClickListener

class CurrencySearchAdapter(private var listener: onItemClickListener) :
    RecyclerView.Adapter<CurrencySearchAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: CurrencyDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            CurrencyDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    private var itemList = emptyList<RatesData>()

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currenntItem = itemList[position]
        holder.binding.apply {
            currencyNameDialogItemTextView.text = currenntItem.id
            root.setOnClickListener {
                listener.onItemClick(position, root)
            }
        }
    }

    fun setData(newItemList: List<RatesData>) {
        val diffUtil = DiffUtil(itemList, newItemList)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, itemList.size)
    }
}