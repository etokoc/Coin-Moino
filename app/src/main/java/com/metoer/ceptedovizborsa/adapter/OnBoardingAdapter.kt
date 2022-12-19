package com.metoer.ceptedovizborsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metoer.ceptedovizborsa.data.response.onboarding.OnBoardingItem
import com.metoer.ceptedovizborsa.databinding.ItemOnboardingBinding

class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var itemList = emptyList<OnBoardingItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.apply {
            onboardingImage.setImageResource(currentItem.imageResource)
            textViewHeader.text = currentItem.title
            textViewDescription.text = currentItem.description
        }
    }

    fun setData(newItemList: List<OnBoardingItem>) {
        itemList = newItemList
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}