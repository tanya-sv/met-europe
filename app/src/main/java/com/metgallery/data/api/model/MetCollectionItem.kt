package com.metgallery.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MetCollectionItem(
    val title: String,
    val description: String,
    val artist: String,
    val teaserText: String,
    val url: String,
    val image: String,
    val regularImage: String,
    val largeImage: String,
    val date: String?,
    val medium: String,
    val galleryInformation: String
) : Parcelable
