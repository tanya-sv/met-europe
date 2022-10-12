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

    //TODO retrieve random image from the db
    private val _randomUrl = MutableLiveData<String>().apply { value = "https://images.metmuseum.org/CRDImages/ep/web-large/DP-416-001.jpg" }
    val randomUrl: LiveData<String> = _randomUrl

    var selectedEra = EuropeanCollectionEra.None
    var selectedArtistNationality = ArtistNationality.None

}