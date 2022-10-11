package com.metgallery.data

import androidx.room.*
import com.metgallery.data.model.MetCollectionItem

@Dao
interface MetCollectionDao {

    @Query("SELECT COUNT(objectId) FROM met_collection_item")
    suspend fun getCount(): Int

    @Query("SELECT * FROM met_collection_item")
    suspend fun getAll(): List<MetCollectionItem>

    @Query("SELECT * FROM met_collection_item WHERE artist_nationality LIKE :nationality")
    suspend fun findByArtistNationality(nationality: String): List<MetCollectionItem>

    @Query("SELECT * FROM met_collection_item WHERE object_begin_date >= :beginData AND object_end_date <= :endDate")
    suspend fun findByEra(beginData: Int, endDate: Int): List<MetCollectionItem>

    @Query("SELECT * FROM met_collection_item WHERE artist_nationality LIKE :nationality AND object_begin_date >= :beginData AND object_end_date <= :endDate")
    suspend fun findByArtistNationalityAndEra(
        nationality: String,
        beginData: Int,
        endDate: Int
    ): List<MetCollectionItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<MetCollectionItem>)

    @Update
    suspend fun update(item: MetCollectionItem)

}