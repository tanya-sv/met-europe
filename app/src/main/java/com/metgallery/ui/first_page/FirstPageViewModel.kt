package com.metgallery.ui.first_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
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
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP353257.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/Wrightsman93.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP169564.jpg",
        "https://images.metmuseum.org/CRDImages/ep/web-large/DP320086.jpg")

    private val _randomUrl = MutableLiveData<String>().apply { value = randomImages.random() }
    val randomUrl: LiveData<String> = _randomUrl

    var selectedEra = EuropeanCollectionEra.None
    var selectedArtistNationality = ArtistNationality.None
    var excludeMiniatures: Boolean = false

}