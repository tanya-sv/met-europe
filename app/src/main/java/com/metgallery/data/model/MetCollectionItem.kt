package com.metgallery.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MetCollectionItem(val objectId: Int, val image: String) : Parcelable
