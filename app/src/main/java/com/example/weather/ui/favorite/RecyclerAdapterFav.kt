package com.example.weather.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productslist.FavDiffUtil
import com.example.weather.databinding.CardFavLocationsBinding
import com.example.weather.model.DisplayableDailyForecast
import com.example.weather.model.DisplayableWeatherData


class RecyclerAdapterFav() :
    ListAdapter<DisplayableWeatherData, RecyclerAdapterFav.ViewHolder>(FavDiffUtil()) {
    lateinit var binding: CardFavLocationsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        binding = CardFavLocationsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current: DisplayableWeatherData = getItem(position)
        holder.binding.txtCountry.text = current.locationDescription
    }

    class ViewHolder(var binding: CardFavLocationsBinding) : RecyclerView.ViewHolder(binding.root)
}

