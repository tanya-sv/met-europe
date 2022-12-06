package com.metgallery.ui.item_details

import androidx.lifecycle.*
import com.metgallery.data.CollectionRepository
import com.metgallery.data.Result
import com.metgallery.data.model.MetCollectionFavourite
import com.metgallery.data.model.MetObject
import com.metgallery.data.model.Tag
import com.metgallery.ui.R
import com.metgallery.util.Event
import com.metgallery.util.ImageLoadingCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _itemDetails = MutableLiveData<MetObject>().apply { value = MetObject() }
    val itemDetails: LiveData<MetObject?> = _itemDetails

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _isFavourite = MutableLiveData<Boolean>()

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _imageLoading = MutableLiveData<Event<Boolean>>()
    val imageLoading: LiveData<Event<Boolean>> = _imageLoading

    private val _dataLoading = MutableLiveData<Boolean>()

    //emits true while either image or item details is being loaded
    fun getLoadingStatus(): LiveData<Boolean> {
        val mediatorLiveData = MediatorLiveData<Boolean>()

        var isFirstEmitted = false
        var isSecondEmitted = false

        mediatorLiveData.addSource(_imageLoading) {
            isFirstEmitted = true
            if (isFirstEmitted && isSecondEmitted) {
                mediatorLiveData.value = _imageLoading.value?.peekContent() ?: false || _dataLoading.value ?: false
            }
        }
        mediatorLiveData.addSource(_dataLoading) {
            isSecondEmitted = true
            if (isFirstEmitted && isSecondEmitted) {
                mediatorLiveData.value = _imageLoading.value?.peekContent() ?: false || _dataLoading.value ?: false
            }
        }
        return mediatorLiveData
    }

    private val _selectedTag = MutableLiveData<Event<Tag>>()
    val selectedTag: LiveData<Event<Tag>> = _selectedTag

    private val _selectedArtist = MutableLiveData<Event<String>>()
    val selectedArtist: LiveData<Event<String>> = _selectedArtist

    val imageLoadingCallback: ImageLoadingCallback = object : ImageLoadingCallback {
        override fun onLoading() {
            _imageLoading.value = Event(true)
        }

        override fun onSuccess() {
            _imageLoading.value = Event(false)
        }

        override fun onError() {
            _imageLoading.value = Event(false)
            _snackbarText.value = Event(R.string.error_loading_image)
        }
    }

    fun onTagClicked(tag: Tag) {
        _selectedTag.value = Event(tag)
    }

    fun onArtistClicked(artist: String) {
        _selectedArtist.value = Event(artist)
    }

    fun start(objectId: Int, favourite: Boolean) {
        if (_isDataAvailable.value == true || _dataLoading.value == true) {
            return
        }
        _dataLoading.value = true
        _isFavourite.value = favourite

        viewModelScope.launch {
            val result = collectionRepository.getObjectDetailsById(objectId)

            if (result is Result.Error) {
                _itemDetails.value = MetObject()
                _isDataAvailable.value = false
                _imageLoading.value = Event(false)
                _snackbarText.value = Event(R.string.error_loading_data)

            } else if (result is Result.Success) {
                _itemDetails.value = result.data
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