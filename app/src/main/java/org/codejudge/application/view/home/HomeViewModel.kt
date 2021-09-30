package org.codejudge.application.view.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.codejudge.application.data.repository.PlaceRepository
import org.codejudge.application.model.ui.NearbyPlace
import org.codejudge.application.utils.network.Result

class HomeViewModel @ViewModelInject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {
    private val _nearbyPlaceResponse = MutableLiveData<Result<List<NearbyPlace>>>()
    private val _isSearchValid = MutableLiveData<Boolean>()

    val nearbyPlaceResponse: LiveData<Result<List<NearbyPlace>>>
        get() = _nearbyPlaceResponse
    val isSearchValid: LiveData<Boolean>
        get() = _isSearchValid

    var keyword = ""
        set(value) {
            field = value
            validateSearch()
        }

    init {
        requestNearbyPlaces(null)
    }

    private fun validateSearch() {
        if (keyword.isNotEmpty()) {
            _isSearchValid.postValue(true)
            requestNearbyPlaces(keyword)
        } else {
            _isSearchValid.postValue(false)
            requestNearbyPlaces(null)
        }
    }

    fun requestNearbyPlaces(keyword: String?) {
        viewModelScope.launch {
            placeRepository.requestNearbyPlaces(keyword)
                .collect {
                    _nearbyPlaceResponse.value = it
                }
        }
    }
}