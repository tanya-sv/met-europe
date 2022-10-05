package com.metgallery.ui.first_page

import androidx.lifecycle.ViewModel
import com.metgallery.data.CollectionRepository
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstPageViewModel @Inject constructor(private val collectionRepository: CollectionRepository) :
    ViewModel() {

    var selectedEra = EuropeanCollectionEra.None
    var selectedArtistNationality = ArtistNationality.None

    fun randomImageUrl(): String {
        return collectionRepository.randomImageUrl()
    }
}