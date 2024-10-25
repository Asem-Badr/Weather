package com.example.weather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productslist.HourlyDiffUtil
import com.example.weather.databinding.CardHourlyDataBinding
import com.example.weather.model.HourlyData


class RecyclerAdapterHourlyData() :
    ListAdapter<HourlyData, RecyclerAdapterHourlyData.ViewHolder>(HourlyDiffUtil()) {
    lateinit var binding: CardHourlyDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        binding = CardHourlyDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current: HourlyData = getItem(position)
        holder.binding.txtCardTemp.text = current.temp
        holder.binding.txtCardTime.text = current.time
    }

    class ViewHolder(var binding: CardHourlyDataBinding) : RecyclerView.ViewHolder(binding.root)
}

