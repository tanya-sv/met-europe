package com.metgallery.ui.first_page

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
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

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).into(imageView);
}