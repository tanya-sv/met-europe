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
        era: EuropeanCollectionEra,
        excludeMiniatures: Boolean
    ): List<MetCollectionItem> {

        if (artistNationality != ArtistNationality.None && era != EuropeanCollectionEra.None) {
            return if (excludeMiniatures) {
                metCollectionDao.findByArtistNationalityAndEraExcludeMiniatures(
                    artistNationality.displayValue,
                    era.dateBegin,
                    era.dateEnd
                )
            } else {
                metCollectionDao.findByArtistNationalityAndEra(
                    artistNationality.displayValue,
                    era.dateBegin,
                    era.dateEnd
                )
            }
        }
        if (artistNationality != ArtistNationality.None) {
            return if (excludeMiniatures) metCollectionDao.findByArtistNationalityExcludeMiniatures(
                artistNationality.displayValue
            ) else
                metCollectionDao.findByArtistNationality(artistNationality.displayValue)
        }

        if (era != EuropeanCollectionEra.None) {
            return if (excludeMiniatures) metCollectionDao.findByEraExcludeMiniatures(
                era.dateBegin,
                era.dateEnd
            ) else metCollectionDao.findByEra(era.dateBegin, era.dateEnd)
        }
        return if (excludeMiniatures) metCollectionDao.getAllExcludeMiniatures() else metCollectionDao.getAll()
    }

    suspend fun searchByTag(tag: String): List<MetCollectionItem> {
        return metCollectionDao.findByTag(tag)
    }

    suspend fun getObjectDetailsById(objectId: Int): MetObject? {
        return withContext(Dispatchers.IO) {
            metMuseumApi.getObjectById(objectId).body()
        }
    }

    suspend fun getFavourites() : List<MetCollectionItem>  {
        return metCollectionDao.getAllFavourites()
    }

    suspend fun updateFavourite(favourite: MetCollectionFavourite) {
        metCollectionDao.update(favourite)
    }
}