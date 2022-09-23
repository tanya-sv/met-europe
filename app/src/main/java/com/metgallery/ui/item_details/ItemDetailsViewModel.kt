package com.metgallery.ui.item_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.api.model.MetCollectionItem
import com.metgallery.data.api.model.MetObject
import com.metgallery.domain.GetObjectDetailsById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val getObjectDetailsById: GetObjectDetailsById) :
    ViewModel() {

    private val _itemDetails = MutableLiveData<MetObject>()
    val itemDetails: LiveData<MetObject> = _itemDetails

    private val _item = MutableLiveData<MetCollectionItem>()
    val item: LiveData<MetCollectionItem> = _item

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun start(collectionItem: MetCollectionItem) {
        _dataLoading.value = true
        _item.value = collectionItem

        val objectId = collectionItem.url
            .substringAfter("/art/collection/search/")
            .substringBefore("?").toInt()

        viewModelScope.launch {
            val result = getObjectDetailsById(objectId)
            result?.let {
                _itemDetails.value = it
            }
            _dataLoading.value = false
        }
    }

}