package com.metgallery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.SearchResult
import com.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<SearchResult>>().apply { value = emptyList() }
    val items: LiveData<List<SearchResult>> = _items

    private val _selectedSearchResult = MutableLiveData<Event<SearchResult>>()
    val selectedSearchResult: LiveData<Event<SearchResult>> = _selectedSearchResult

    fun search(term: String) {
        viewModelScope.launch  {
            _items.value = collectionRepository.getCountBySearchTerm(term)
        }
    }

    fun onItemClicked(item: SearchResult) {
        _selectedSearchResult.value = Event(item)
    }
}