package com.metoer.ceptedovizborsa.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.databinding.CoinAsksItemBinding
import com.metoer.ceptedovizborsa.databinding.CoinBidsItemBinding
import com.metoer.ceptedovizborsa.util.Constants.MINIMUM_DEPTH_WIDTH
import com.metoer.ceptedovizborsa.util.patternText

class CoinDepthAdapter(var enum: DepthEnum) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var askQuantityList = ArrayList<Double>()
    private var bidsQuantityList = ArrayList<Double>()
    private var _itemList = ArrayList<ArrayList<String>>()
    fun setData(itemList: List<ArrayList<String>>) {
        _itemList.clear()
        _itemList.addAll(itemList)
        calculateQuantity(0)
        calculateQuantity(1)
        notifyDataSetChanged()
    }

    private fun calculateQuantity(quantityType: Int) {
        val doubleValueList = ArrayList<Double>()
        for (index in 0 until _itemList.size) {
            if (quantityType == 0)
                doubleValueList.add(_itemList[index][1].toDouble())
            else
                doubleValueList.add(_itemList[index][0].toDouble())
        }
        val max: Double = doubleValueList.maxOrNull() ?: 0.0
        val min: Double = doubleValueList.minOrNull() ?: 0.0
        if (quantityType == 0) {
            askQuantityList.clear()
            askQuantityList.addAll(doubleValueList.map { (it - min) / (max - min) })
        } else {
            bidsQuantityList.clear()
            bidsQuantityList.addAll(doubleValueList.map { (it - min) / (max - min) })
        }
    }

    inner class LayoutOneViewHolder(val itemBinding: CoinAsksItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            val quantity = _itemList[position].get(0)
            val value = _itemList[position].get(1)
            itemBinding.textViewAsksQuantity.patternText(quantity, "###,###,###,###.######")
            itemBinding.textViewAsksValue.patternText(value, "###,###,###,###.########")
            val displayMetrics = itemBinding.root.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val viewWidth =
                (MINIMUM_DEPTH_WIDTH + (screenWidth - MINIMUM_DEPTH_WIDTH) * (askQuantityList[position])).toInt()
            itemBinding.asksBackgroundView.layoutParams =
                FrameLayout.LayoutParams(viewWidth, LayoutParams.WRAP_CONTENT)
        }
    }

    inner class LayoutTwoViewHolder(val itemBinding: CoinBidsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            val quantity = _itemList[position].get(1)
            val value = _itemList[position].get(0)
            itemBinding.textViewBidsQuantity.patternText(quantity, "###,###,###,###.######")
            itemBinding.textViewBidsValue.patternText(value, "###,###,###,###.########")
            val displayMetrics = itemBinding.root.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val viewWidth =
                (MINIMUM_DEPTH_WIDTH + (screenWidth - MINIMUM_DEPTH_WIDTH) * (askQuantityList[position])).toInt()
            val _layoutParams = FrameLayout.LayoutParams(viewWidth, LayoutParams.WRAP_CONTENT)
            _layoutParams.gravity = Gravity.END
            itemBinding.bidsBackgroundView.layoutParams = _layoutParams
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