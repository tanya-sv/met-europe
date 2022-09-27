package com.metgallery.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.api.model.MetCollectionItem
import com.metgallery.domain.GetObjectsByDepartmentId
import com.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val getObjectsByDepartmentId: GetObjectsByDepartmentId) : ViewModel() {

    private val _items = MutableLiveData<List<MetCollectionItem>>().apply { value = emptyList() }
    val items: LiveData<List<MetCollectionItem>> = _items

    private val _selectedItem = MutableLiveData<Event<MetCollectionItem>>()
    val selectedItem: LiveData<Event<MetCollectionItem>> = _selectedItem

    fun selectItem(item: MetCollectionItem) {
        _selectedItem.value = Event(item)
    }

    fun loadCollection(departmentId: Int) {
        viewModelScope.launch {
            //_items.value = getCollectionByQuery(departmentId)
        }
    }

}
