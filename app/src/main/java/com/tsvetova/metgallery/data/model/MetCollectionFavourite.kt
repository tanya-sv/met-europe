package com.tsvetova.metgallery.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetCollectionFavourite(
    @PrimaryKey val objectId: Int,
    @ColumnInfo(name = "favourite") val favourite: Boolean
)
