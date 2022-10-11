package com.metgallery.data

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.data.model.MetObject
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

    //TODO support paging
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

    suspend fun getObjectDetailsById(objectId: Int): MetObject? {
        return withContext(Dispatchers.IO) {
            metMuseumApi.getObjectById(objectId).body()
        }
    }
}