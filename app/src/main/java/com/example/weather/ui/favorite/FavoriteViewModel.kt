package com.example.weather.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.repository.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val _repo: IRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private var _locations : MutableLiveData<List<DisplayableWeatherData>> = MutableLiveData<List<DisplayableWeatherData>>()
    val locations : LiveData<List<DisplayableWeatherData>> = _locations

    fun getFavoriteLocations(){
        viewModelScope.launch(Dispatchers.IO)  {
            _repo.getFavoriteLocations().collect{
                _locations.postValue(it)
            }
        }
    }
    fun addToFav(location: DisplayableWeatherData){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.addToFav(location)
            getFavoriteLocations()
        }
    }
    fun removeFromFav(location: DisplayableWeatherData){
        viewModelScope.launch(Dispatchers.IO)  {
            _repo.removeFromFav(location)
            getFavoriteLocations()
        }
    }
}

class FavoriteViewModelFactory(private val _repo: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}