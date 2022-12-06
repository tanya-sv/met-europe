package com.tsvetova.metgallery.ui.first_page

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsvetova.metgallery.data.CollectionRepository
import com.tsvetova.metgallery.data.model.ArtistNationality
import com.tsvetova.metgallery.data.model.EuropeanCollectionEra
import com.tsvetova.metgallery.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstPageViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    private val randomImages = listOf(
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP-416-001.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP243354.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DT2818.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DT2566.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP169402.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP-406-01.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP167132.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP169564.jpg"
    )

    private val _randomUrl = MutableLiveData<String>().apply { value = randomImages.random() }
    val randomUrl: LiveData<String> = _randomUrl

    private var selectedEra = EuropeanCollectionEra.None
    private var selectedArtistNationality = ArtistNationality.None

    private val _selectedFilters = MutableLiveData<Event<Pair<EuropeanCollectionEra, ArtistNationality>>>()
    val selectedFilters: LiveData<Event<Pair<EuropeanCollectionEra, ArtistNationality>>> = _selectedFilters

    fun onEraSelected(av: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedEra = EuropeanCollectionEra.values()[position]
    }

    fun onArtistNationalitySelected(av: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedArtistNationality = ArtistNationality.values()[position]
    }

    fun onExploreClicked() {
        _selectedFilters.value = Event(Pair(selectedEra, selectedArtistNationality))
    }

}