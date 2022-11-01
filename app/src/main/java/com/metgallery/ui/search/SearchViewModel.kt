package com.metgallery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.SearchTag
import com.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<SearchTag>>().apply { value = emptyList() }
    val items: LiveData<List<SearchTag>> = _items

    private val _selectedSearchTag = MutableLiveData<Event<SearchTag>>()
    val selectedSearchTag: LiveData<Event<SearchTag>> = _selectedSearchTag

    fun searchTag(term: String) {
        viewModelScope.launch  {
            _items.value = collectionRepository.getCountByTag(term)
        }
    }

    fun onItemClicked(item: SearchTag) {
        _selectedSearchTag.value = Event(item)
    }

}