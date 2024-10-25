package com.example.weather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productslist.DailyDiffUtil
import com.example.weather.databinding.CardDailyDataBinding
import com.example.weather.model.DailyData


class RecyclerAdapterDailyData() :
    ListAdapter<DailyData, RecyclerAdapterDailyData.ViewHolder>(DailyDiffUtil()) {
    lateinit var binding: CardDailyDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        binding = CardDailyDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current: DailyData = getItem(position)
        holder.binding.txtCardDailyDate.text = current.date
        holder.binding.txtCardDailyDay.text = current.day
    }

    class ViewHolder(var binding: CardDailyDataBinding) : RecyclerView.ViewHolder(binding.root)
}

