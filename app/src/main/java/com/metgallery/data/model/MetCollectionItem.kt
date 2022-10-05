package com.metgallery.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MetCollectionItem(
    val objectId: Int,
    val title: String,
    val artist: String,
    val artistNationality: String,
    val objectBeginDate: Int,
    val objectEndDate: Int,
    val medium: String,
    val tags: List<String>,
    val imageUrl: String,
    val height: Float,
    val width: Float
) : Parcelable
