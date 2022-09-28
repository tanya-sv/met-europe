package com.metgallery.ui.item_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.model.MetObject
import com.metgallery.domain.GetObjectDetailsById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val getObjectDetailsById: GetObjectDetailsById) :
    ViewModel() {

    private val _itemDetails = MutableLiveData<MetObject>().apply { value = MetObject() }
    val itemDetails: LiveData<MetObject?> = _itemDetails

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun start(objectId: Int) {
        if (_isDataAvailable.value == true || _dataLoading.value == true) {
            return
        }

        _dataLoading.value = true

        viewModelScope.launch {
            val result = getObjectDetailsById(objectId)

            if (result == null) {
                _itemDetails.value = null
                _isDataAvailable.value = false
            } else {
                _itemDetails.value = result
                _isDataAvailable.value = true
            }

            _dataLoading.value = false
        }
    }

}