package com.example.productslist

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.model.DisplayableWeatherData

class FavDiffUtil : DiffUtil.ItemCallback<DisplayableWeatherData>() {
    override fun areItemsTheSame(oldItem: DisplayableWeatherData, newItem: DisplayableWeatherData): Boolean {
        return oldItem.locationDescription == newItem.locationDescription
    }

    override fun areContentsTheSame(oldItem: DisplayableWeatherData, newItem: DisplayableWeatherData): Boolean {
        return oldItem == newItem
    }
}