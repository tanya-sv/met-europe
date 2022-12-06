package com.tsvetova.metgallery.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tsvetova.metgallery.data.model.Converters
import com.tsvetova.metgallery.data.model.MetCollectionItem

@Database(entities = [MetCollectionItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun metCollectionDao(): MetCollectionDao

}