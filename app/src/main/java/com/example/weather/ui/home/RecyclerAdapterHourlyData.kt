package com.example.weather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productslist.HourlyDiffUtil
import com.example.weather.databinding.CardHourlyDataBinding
import com.example.weather.model.DisplayableHourlyForecast


class RecyclerAdapterHourlyData() :
    ListAdapter<DisplayableHourlyForecast, RecyclerAdapterHourlyData.ViewHolder>(HourlyDiffUtil()) {
    lateinit var binding: CardHourlyDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        binding = CardHourlyDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current: DisplayableHourlyForecast = getItem(position)
        holder.binding.txtCardTemp.text = current.temp
        holder.binding.txtCardTime.text = current.time
        Glide.with(holder.binding.root.context)
            .load(current.iconUrl)
            .into(holder.binding.imageViewHourlyDataIcon)
    }

    class ViewHolder(var binding: CardHourlyDataBinding) : RecyclerView.ViewHolder(binding.root)
}

