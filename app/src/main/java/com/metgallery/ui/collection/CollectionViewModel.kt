package com.metgallery.ui.collection

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import com.metgallery.data.model.MetCollectionFavourite
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.util.Consts.ARTIST
import com.metgallery.util.Consts.ERA
import com.metgallery.util.Consts.FAVOURITES_ONLY
import com.metgallery.util.Consts.NATIONALITY
import com.metgallery.util.Consts.TAG
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

    private var _era: EuropeanCollectionEra = EuropeanCollectionEra.None
    fun getEra(): EuropeanCollectionEra = _era

    private var _artistNationality: ArtistNationality = ArtistNationality.None
    fun getArtistNationality(): ArtistNationality = _artistNationality

    private var _favouritesOnly: Boolean = false
    fun isFavouritesOnly(): Boolean = _favouritesOnly

    private var _tag: String? = null
    fun getTag(): String? = _tag

    private var _artist: String? = null
    fun getArtist(): String? = _artist

    fun readFromBundle(bundle: Bundle) {
        bundle.getSerializable(ERA)?.let {
            _era = it as EuropeanCollectionEra
        }
        bundle.getSerializable(NATIONALITY)?.let {
            _artistNationality = it as ArtistNationality
        }
        _favouritesOnly = bundle.getBoolean(FAVOURITES_ONLY)
        _tag = bundle.getString(TAG)
        _artist = bundle.getString(ARTIST)
    }

    fun selectItem(item: MetCollectionItem) {
        _selectedItem.value = Event(item)
    }

    fun loadCollection() {
        viewModelScope.launch {
            _items.value =
                if (_favouritesOnly) collectionRepository.getFavourites()
                else if (!_tag.isNullOrBlank()) {
                    collectionRepository.searchByTag(_tag!!)
                } else if (!_artist.isNullOrBlank()) {
                    collectionRepository.searchByArtist(_artist!!)
                } else
                    collectionRepository.searchEuropeanPaintings(_artistNationality, _era)
        }
    }

    fun favouriteChanged(item: MetCollectionItem, checked: Boolean) {
        if (item.favourite != checked) {
            item.favourite = checked

            viewModelScope.launch {
                collectionRepository.updateFavourite(MetCollectionFavourite(item.objectId, checked))
            }

            if (isFavouritesOnly() && !item.favourite) {
                viewModelScope.launch {
                    _items.value = collectionRepository.getFavourites()
                }
            }
        }
    }

}
