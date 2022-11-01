package com.metgallery.ui.item_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.MetCollectionFavourite
import com.metgallery.data.model.MetObject
import com.metgallery.data.model.Tag
import com.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _itemDetails = MutableLiveData<MetObject>().apply { value = MetObject() }
    val itemDetails: LiveData<MetObject?> = _itemDetails

    private val _isFavourite = MutableLiveData<Boolean>()

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _selectedTag = MutableLiveData<Event<Tag>>()
    val selectedTag: LiveData<Event<Tag>> = _selectedTag

    fun onTagClicked(tag: Tag) {
        _selectedTag.value = Event(tag)
    }

    fun start(objectId: Int, favourite: Boolean) {
        if (_isDataAvailable.value == true || _dataLoading.value == true) {
            return
        }
        _dataLoading.value = true
        _isFavourite.value = favourite

        viewModelScope.launch {
            val result = collectionRepository.getObjectDetailsById(objectId)

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

    fun getIsFavourite(): Boolean {
        return _isFavourite.value ?: false
    }

    fun setIsFavourite(value: Boolean) {
        // Avoids infinite loops
        if (_isFavourite.value != value) {
            _isFavourite.value = value

            _itemDetails.value?.let {
                viewModelScope.launch {
                    collectionRepository.updateFavourite(MetCollectionFavourite(it.objectID, value))
                }
            }
        }
    }

}