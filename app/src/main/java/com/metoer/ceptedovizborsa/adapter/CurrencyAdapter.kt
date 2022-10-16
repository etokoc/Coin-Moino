package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.util.Constants
import kotlinx.android.synthetic.main.currency_item_list.view.*
import java.text.DecimalFormat
import java.util.*

class CurrencyAdapter(
    var items: List<Currency>
) : RecyclerView.Adapter<CurrencyAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun filterList(filterList: List<Currency>) {
        items = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItems = items[position]
        holder.itemView.apply {
            moneyNameTextView.text = currentItems.Isim
            moneyCodeTextView.text = currentItems.CurrencyCode
            val result = currentItems.ForexBuying?.div(currentItems.Unit!!)!!
            moneyValueTextView.text = "₺"+DecimalFormat("##.####").format(result)
            Glide.with(this).load(
                Constants.IMAGE_URL + "${
                    currentItems.CurrencyCode?.lowercase(
                        Locale.ENGLISH
                    )
                }.png"
            )
                .into(moneyImage)
        }
    }

    override fun getItemCount(): Int {
        //XDR para birimini almamak için yaptık
        return items.size
    }
}