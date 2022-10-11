package com.metgallery.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "met_collection_item")
@Parcelize
data class MetCollectionItem(
    @PrimaryKey val objectId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "artist_nationality") val artistNationality: String,
    @ColumnInfo(name = "object_begin_date") val objectBeginDate: Int,
    @ColumnInfo(name = "object_end_date") val objectEndDate: Int,
    @ColumnInfo(name = "medium") val medium: String,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "width") val width: Float
) : Parcelable
