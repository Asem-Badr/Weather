package com.example.productslist

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.model.DailyData

class DailyDiffUtil : DiffUtil.ItemCallback<DailyData>() {
    override fun areItemsTheSame(oldItem: DailyData, newItem: DailyData): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyData, newItem: DailyData): Boolean {
        return oldItem == newItem
    }
}