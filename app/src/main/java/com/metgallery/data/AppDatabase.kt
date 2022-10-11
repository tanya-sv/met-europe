package com.metgallery.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.metgallery.data.model.Converters
import com.metgallery.data.model.MetCollectionItem

@Database(entities = [MetCollectionItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun metCollectionDao(): MetCollectionDao

}