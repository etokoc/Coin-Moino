package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.data.response.coin.depth.CoinDepth
import com.metoer.ceptedovizborsa.databinding.CoinAsksItemBinding
import com.metoer.ceptedovizborsa.databinding.CoinBidsItemBinding
import com.metoer.ceptedovizborsa.util.NumberDecimalFormat

class CoinDepthAdapter(var enum: DepthEnum) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemList = ArrayList<ArrayList<String>>()
    fun setData(itemList: List<ArrayList<String>>) {
        _itemList.clear()
        _itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    inner class LayoutOneViewHolder(val itemBinding: CoinAsksItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            val quantity = _itemList[position].get(0)
            val value = _itemList[position].get(1)
            itemBinding.textViewAsksQuantity.text = NumberDecimalFormat.numberDecimalFormat(quantity,"###,###,###,###.######")
            itemBinding.textViewAsksValue.text = NumberDecimalFormat.numberDecimalFormat(value,"###,###,###,###.######")
        }
    }

    inner class LayoutTwoViewHolder(val itemBinding: CoinBidsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            val quantity = _itemList[position].get(1)
            val value = _itemList[position].get(0)
            itemBinding.textViewBidsQuantity.text = NumberDecimalFormat.numberDecimalFormat(quantity,"###,###,###,###.######")
            itemBinding.textViewBidsValue.text = NumberDecimalFormat.numberDecimalFormat(value,"###,###,###,###.######")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (enum) {
            DepthEnum.ASKS -> CoinAsksItemBinding.inflate(inflater, parent, false)
            DepthEnum.BIDS -> CoinBidsItemBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return when (enum) {
            DepthEnum.ASKS -> LayoutOneViewHolder(binding as CoinAsksItemBinding)
            DepthEnum.BIDS -> LayoutTwoViewHolder(binding as CoinBidsItemBinding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return _itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (enum) {
            DepthEnum.ASKS -> (holder as LayoutOneViewHolder).bind(position)
            DepthEnum.BIDS -> (holder as LayoutTwoViewHolder).bind(position)
        }
    }

}

enum class DepthEnum {
    BIDS, ASKS
}