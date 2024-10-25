package com.example.productslist

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.model.HourlyData

class HourlyDiffUtil : DiffUtil.ItemCallback<HourlyData>() {
    override fun areItemsTheSame(oldItem: HourlyData, newItem: HourlyData): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: HourlyData, newItem: HourlyData): Boolean {
        return oldItem == newItem
    }
}