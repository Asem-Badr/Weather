package com.example.weather.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.database.LocationsDatabase
import com.example.weather.SettingsManager
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.network.WeatherApiService
import com.example.weather.repository.Repository

class FavoriteFragment : Fragment() {

    lateinit var favoriteViewModel: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerViewFav: RecyclerView
    lateinit var recyclerAdapterFav: RecyclerAdapterFav

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val settingsManager = SettingsManager(requireActivity())
        val favoriteViewModelFactory =
            FavoriteViewModelFactory(
                Repository(
                    WeatherApiService.RetrofitHelper,
                    LocationsDatabase.getInstance(requireContext()).getLocationsDao(),
                    settingsManager
                )
            )
        favoriteViewModel = ViewModelProvider(
            requireActivity(),
            favoriteViewModelFactory
        ).get(FavoriteViewModel::class.java)
        recyclerViewFav = binding.recyclerViewFav

        recyclerAdapterFav = RecyclerAdapterFav()
        recyclerViewFav.apply {
            adapter = recyclerAdapterFav
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
        favoriteViewModel.getFavoriteLocations()
        favoriteViewModel.locations.observe(requireActivity()) {
            recyclerAdapterFav.submitList(it)
        }


        binding.floatingActionButton.setOnClickListener {
            //add items to favorite list logic
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}