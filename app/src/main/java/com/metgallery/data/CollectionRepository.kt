package com.metgallery.data

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.*
import kotlinx.coroutines.*
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val metMuseumApi: MetMuseumApi,
    private val metCollectionDao: MetCollectionDao
) {
    init {
        //triggers creation and pre-population of the database on first run
        CoroutineScope(Dispatchers.IO).launch {
            metCollectionDao.getCount()
        }
    }

    suspend fun searchEuropeanPaintings(
        artistNationality: ArtistNationality,
        era: EuropeanCollectionEra
    ): List<MetCollectionItem> {

        if (artistNationality != ArtistNationality.None && era != EuropeanCollectionEra.None) {
            return metCollectionDao.findByArtistNationalityAndEra(
                artistNationality.displayValue,
                era.dateBegin,
                era.dateEnd
            )
        }

        if (artistNationality != ArtistNationality.None) {
            return metCollectionDao.findByArtistNationality(artistNationality.displayValue)
        }

        if (era != EuropeanCollectionEra.None) {
            return metCollectionDao.findByEra(era.dateBegin, era.dateEnd)
        }
        return metCollectionDao.getAll()
    }

    suspend fun searchByTag(tag: String): List<MetCollectionItem> {
        return metCollectionDao.getAll().filter { it.tags.map { it.lowercase() }.contains(tag.lowercase()) }
    }

    suspend fun searchByArtist(artist: String): List<MetCollectionItem> {
        return metCollectionDao.findByArtist(artist)
    }

    suspend fun getCountBySearchTerm(term: String): List<SearchResult> {

        val searchTerm = term.lowercase()
        val result = mutableListOf<SearchResult>()

        val all = metCollectionDao.getAll()

        val filteredContains = all.filter { it.tags.map { it.lowercase() }.contains(searchTerm.lowercase()) }
        result.add(TagSearchResult(searchTerm, filteredContains.size))

        val allTags = mutableListOf<String>()
        all.forEach {
            allTags.addAll(it.tags)
        }

        val matchingTags = allTags.toSet().filter { it.lowercase().startsWith(searchTerm.lowercase()) }
        matchingTags.forEach { matchingTag ->
            //avoid duplicates
            if (!result.map { it.term.lowercase() }.contains(matchingTag.lowercase())) {
                result.add(TagSearchResult(matchingTag, all.filter { it.tags.contains(matchingTag) }.size))
            }
        }

        val matchingArtists = all.map { it.artist }.toSet().filter {
            it.lowercase().split(" ").any { it.startsWith(searchTerm.lowercase()) }
        }
        matchingArtists.forEach { matchingArtist ->
            result.add(ArtistSearchResult(matchingArtist, all.filter { it.artist == matchingArtist }.size))
        }

        return result.sortedByDescending { it.resultCount }
    }

    suspend fun getObjectDetailsById(objectId: Int): MetObject? {
        return withContext(Dispatchers.IO) {
            metMuseumApi.getObjectById(objectId).body()
        }
    }

    suspend fun getFavourites(): List<MetCollectionItem> {
        return metCollectionDao.getAllFavourites()
    }

    suspend fun updateFavourite(favourite: MetCollectionFavourite) {
        metCollectionDao.update(favourite)
    }
}