package com.metgallery.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import com.metgallery.data.model.MetCollectionFavourite
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<MetCollectionItem>>().apply { value = emptyList() }
    val items: LiveData<List<MetCollectionItem>> = _items

    private val _selectedItem = MutableLiveData<Event<MetCollectionItem>>()
    val selectedItem: LiveData<Event<MetCollectionItem>> = _selectedItem

    private var favouritesOnly = false

    fun selectItem(item: MetCollectionItem) {
        _selectedItem.value = Event(item)
    }

    fun loadCollection(
        favourites: Boolean,
        artistNationality: ArtistNationality,
        era: EuropeanCollectionEra,
        excludeMiniatures: Boolean
    ) {
        viewModelScope.launch {
            favouritesOnly = favourites

            _items.value =
                if (favourites) collectionRepository.getFavourites()
                else
                    collectionRepository.searchEuropeanPaintings(artistNationality, era, excludeMiniatures)
        }
    }

    fun favouriteChanged(item: MetCollectionItem, checked: Boolean) {
        if (item.favourite != checked) {
            item.favourite = checked

            viewModelScope.launch {
                collectionRepository.updateFavourite(MetCollectionFavourite(item.objectId, checked))
            }

            if (favouritesOnly && !item.favourite) {
                viewModelScope.launch {
                    _items.value = collectionRepository.getFavourites()
                }
            }
        }
    }

}
