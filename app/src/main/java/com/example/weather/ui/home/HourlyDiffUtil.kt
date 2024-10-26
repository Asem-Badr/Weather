package com.example.productslist

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.model.DisplayableHourlyForecast

class HourlyDiffUtil : DiffUtil.ItemCallback<DisplayableHourlyForecast>() {
    override fun areItemsTheSame(oldItem: DisplayableHourlyForecast, newItem: DisplayableHourlyForecast): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: DisplayableHourlyForecast, newItem: DisplayableHourlyForecast): Boolean {
        return oldItem == newItem
    }
}