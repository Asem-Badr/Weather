package com.example.productslist

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.model.DisplayableDailyForecast

class DailyDiffUtil : DiffUtil.ItemCallback<DisplayableDailyForecast>() {
    override fun areItemsTheSame(oldItem: DisplayableDailyForecast, newItem: DisplayableDailyForecast): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(oldItem: DisplayableDailyForecast, newItem: DisplayableDailyForecast): Boolean {
        return oldItem == newItem
    }
}