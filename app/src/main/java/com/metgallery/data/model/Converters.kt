package com.metgallery.data.model

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun listToTags(value: List<String>): String = value.joinToString("|")

    @TypeConverter
    fun tagsToList(value: String): List<String> = value.split("|")

}