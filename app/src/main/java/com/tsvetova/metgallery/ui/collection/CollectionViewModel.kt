package com.tsvetova.metgallery.ui.collection

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsvetova.metgallery.data.CollectionRepository
import com.tsvetova.metgallery.data.model.ArtistNationality
import com.tsvetova.metgallery.data.model.EuropeanCollectionEra
import com.tsvetova.metgallery.data.model.MetCollectionFavourite
import com.tsvetova.metgallery.data.model.MetCollectionItem
import com.tsvetova.metgallery.data.Result
import com.tsvetova.metgallery.ui.R
import com.tsvetova.metgallery.util.Consts.ARTIST
import com.tsvetova.metgallery.util.Consts.ERA
import com.tsvetova.metgallery.util.Consts.FAVOURITES_ONLY
import com.tsvetova.metgallery.util.Consts.NATIONALITY
import com.tsvetova.metgallery.util.Consts.TAG
import com.tsvetova.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<MetCollectionItem>>().apply { value = emptyList() }
    val items: LiveData<List<MetCollectionItem>> = _items

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

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
            val result: Result<List<MetCollectionItem>> =
                if (_favouritesOnly) collectionRepository.getFavourites()
                else if (!_tag.isNullOrBlank()) {
                    collectionRepository.searchByTag(_tag!!)
                } else if (!_artist.isNullOrBlank()) {
                    collectionRepository.searchByArtist(_artist!!)
                } else
                    collectionRepository.searchEuropeanPaintings(_artistNationality, _era)

            if (result is Result.Success) {
                _items.value = result.data
            } else if (result is Result.Error) {
                _snackbarText.value =
                    Event(if (_favouritesOnly) R.string.error_loading_favourites else R.string.error_loading_data)
            }
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

                    val result = collectionRepository.getFavourites()
                    if (result is Result.Success) {
                        _items.value = result.data
                    } else if (result is Result.Error) {
                        _snackbarText.value = Event(R.string.error_loading_favourites)
                    }
                }
            }
        }
    }
}
