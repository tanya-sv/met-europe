package com.metgallery.data

import android.content.Context
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.data.model.MetObject
import kotlinx.coroutines.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.QuoteMode
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val metMuseumApi: MetMuseumApi,
    private val metCollectionDao: MetCollectionDao,
    context: Context
) {
    //TODO inject dispatchers?
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        prepopulateDataBaseIfNeeded(context)
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
            return@withContext metMuseumApi.getObjectById(objectId).body()
        }
    }

    private fun prepopulateDataBaseIfNeeded(context: Context) {
        mainScope.launch {

            withContext(ioScope.coroutineContext) {

                if (metCollectionDao.getCount() == 0) {
                    val metObjects = mutableListOf<MetCollectionItem>()

                    //TODO address blocking warnings
                    context.assets.open("met_european_paintings.csv").bufferedReader().use {

                        val csvFormat = CSVFormat.Builder
                            .create()
                            .setDelimiter(';')
                            .setTrim(true)
                            .setQuoteMode(QuoteMode.MINIMAL)
                            .build()

                        CSVFormat.Builder.create(csvFormat).apply {
                            setIgnoreSurroundingSpaces(true)
                        }.build().parse(it)
                            .drop(1) //skip header
                            .map { elements ->

                                metObjects.add(
                                    MetCollectionItem(
                                        objectId = elements[0].toInt(),
                                        title = elements[2],
                                        artist = elements[3],
                                        artistNationality = elements[4],
                                        objectBeginDate = elements[6].toInt(),
                                        objectEndDate = elements[7].toInt(),
                                        medium = elements[8],
                                        tags = elements[10].split("|"),
                                        imageUrl = elements[11],
                                        height = elements[12].toFloat(),
                                        width = elements[13].toFloat()
                                    )
                                )
                            }
                    }
                    metCollectionDao.insertAll(metObjects)
                }
            }
        }
    }
}