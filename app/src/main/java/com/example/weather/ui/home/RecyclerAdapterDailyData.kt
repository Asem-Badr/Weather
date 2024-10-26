package com.example.weather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.productslist.DailyDiffUtil
import com.example.weather.databinding.CardDailyDataBinding
import com.example.weather.model.DisplayableDailyForecast


class RecyclerAdapterDailyData() :
    ListAdapter<DisplayableDailyForecast, RecyclerAdapterDailyData.ViewHolder>(DailyDiffUtil()) {
    lateinit var binding: CardDailyDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        binding = CardDailyDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current: DisplayableDailyForecast = getItem(position)
        holder.binding.txtCardDailyDate.text = current.day
        holder.binding.txtCardDailyTemp.text = current.temp
        Glide.with(holder.binding.root.context)
            .load(current.iconUrl)
            .into(holder.binding.imageViewCardDailyIcon)
    }

    class ViewHolder(var binding: CardDailyDataBinding) : RecyclerView.ViewHolder(binding.root)
}

