package com.tsvetova.metgallery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsvetova.metgallery.data.CollectionRepository
import com.tsvetova.metgallery.data.Result
import com.tsvetova.metgallery.data.model.SearchResult
import com.tsvetova.metgallery.ui.R
import com.tsvetova.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<SearchResult>>().apply { value = emptyList() }
    val items: LiveData<List<SearchResult>> = _items

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _selectedSearchResult = MutableLiveData<Event<SearchResult>>()
    val selectedSearchResult: LiveData<Event<SearchResult>> = _selectedSearchResult

    fun search(term: String) {
        viewModelScope.launch {

            val result = collectionRepository.getCountBySearchTerm(term)
            if (result is Result.Success) {
                _items.value = result.data
            } else {
                _snackbarText.value = Event(R.string.error_loading_search_results)
            }
        }
    }

    fun onItemClicked(item: SearchResult) {
        _selectedSearchResult.value = Event(item)
    }
}