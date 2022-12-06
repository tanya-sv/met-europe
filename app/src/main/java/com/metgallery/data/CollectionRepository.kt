package com.metgallery.data

import android.util.Log
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.*
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val metMuseumApi: MetMuseumApi,
    private val metCollectionDao: MetCollectionDao
) {
    private val TAG = "CollectionRepository"

    init {
        //triggers creation and pre-population of the database on first run
        CoroutineScope(Dispatchers.IO).launch {
            metCollectionDao.getCount()
        }
    }

    suspend fun searchEuropeanPaintings(
        artistNationality: ArtistNationality,
        era: EuropeanCollectionEra
    ): Result<List<MetCollectionItem>> {

        try {
            if (artistNationality != ArtistNationality.None && era != EuropeanCollectionEra.None) {
                return Result.Success(metCollectionDao.findByArtistNationalityAndEra(
                    artistNationality.displayValue,
                    era.dateBegin,
                    era.dateEnd
                ))
            }
            if (artistNationality != ArtistNationality.None) {
                return Result.Success(metCollectionDao.findByArtistNationality(artistNationality.displayValue))
            }
            if (era != EuropeanCollectionEra.None) {
                return Result.Success(metCollectionDao.findByEra(era.dateBegin, era.dateEnd))
            }
            return Result.Success(metCollectionDao.getAll())

        } catch(th: Throwable) {
            Log.e(TAG, th.message ?: "error in searchEuropeanPaintings $era $artistNationality")
            return Result.Error(th)
        }
    }

    suspend fun searchByTag(tag: String): Result<List<MetCollectionItem>> {
        return try {
            Result.Success(
                metCollectionDao.getAll().filter { it.tags.map { it.lowercase() }.contains(tag.lowercase()) })
        } catch (th: Throwable) {
            Log.e(TAG, th.message ?: "error in searchByTag $tag")
            Result.Error(th)
        }
    }

    suspend fun searchByArtist(artist: String): Result<List<MetCollectionItem>> {
        return try {
            return Result.Success(metCollectionDao.findByArtist(artist))
        } catch (th: Throwable) {
            Log.e(TAG, th.message ?: "error in searchByArtist $artist")
            Result.Error(th)
        }
    }

    suspend fun getCountBySearchTerm(term: String): Result<List<SearchResult>> {
        try {
            val searchTerm = term.lowercase()
            val result = mutableListOf<SearchResult>()

            val all = metCollectionDao.getAll()

            val filteredContains =
                all.filter { it.tags.map { it.lowercase() }.contains(searchTerm.lowercase()) }
            result.add(TagSearchResult(searchTerm, filteredContains.size))

            val allTags = mutableListOf<String>()
            all.forEach {
                allTags.addAll(it.tags)
            }

            val matchingTags = allTags.toSet().filter { it.lowercase().startsWith(searchTerm.lowercase()) }
            matchingTags.forEach { matchingTag ->
                //avoid duplicates
                if (!result.map { it.term.lowercase() }.contains(matchingTag.lowercase())) {
                    result.add(
                        TagSearchResult(
                            matchingTag,
                            all.filter { it.tags.contains(matchingTag) }.size
                        )
                    )
                }
            }

            val matchingArtists = all.map { it.artist }.toSet().filter {
                it.lowercase().split(" ").any { it.startsWith(searchTerm.lowercase()) }
            }
            matchingArtists.forEach { matchingArtist ->
                result.add(
                    ArtistSearchResult(
                        matchingArtist,
                        all.filter { it.artist == matchingArtist }.size
                    )
                )
            }

            return Result.Success(result.sortedByDescending { it.resultCount })

        } catch (th: Throwable) {
            Log.e("TAG", th.message ?: "error in getCountBySearchTerm $term")
            return Result.Error(th)
        }
    }

    suspend fun getObjectDetailsById(objectId: Int): Result<MetObject?> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(metMuseumApi.getObjectById(objectId).body())
            } catch (th: Throwable) {
                Log.e("TAG", th.message ?: "error in getObjectDetailsById")
                Result.Error(th)
            }
        }
    }

    suspend fun getFavourites(): Result<List<MetCollectionItem>> {
        return try {
            Result.Success(metCollectionDao.getAllFavourites())
        } catch (th: Throwable) {
            Log.e(TAG, th.message ?: "error in getFavourites")
            Result.Error(th)
        }
    }

    suspend fun updateFavourite(favourite: MetCollectionFavourite) {
        metCollectionDao.update(favourite)
    }
}