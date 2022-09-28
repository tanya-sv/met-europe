package com.metgallery.data

import android.content.Context
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.data.model.MetObject
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.QuoteMode
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val metMuseumApi: MetMuseumApi,
    context: Context
) {
    private val metObjects: MutableMap<Int, MetCollectionItem> = mutableMapOf()

    init {
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

                    val item = MetCollectionItem(
                        objectId = elements[0].toInt(),
                        title = elements[2],
                        artist = elements[3],
                        artistNationality = elements[4],
                        objectBeginDate = elements[6].toInt(),
                        objectEndDate = elements[7].toInt(),
                        medium = elements[8],
                        tags = elements[10].split("|"),
                        imageUrl = elements[11]
                    )

                    metObjects[item.objectId] = item
                }
        }
    }

    fun searchEuropeanPaintings(
        artistNationality: ArtistNationality?,
        era: EuropeanCollectionEra?
    ): List<MetCollectionItem> {

        return metObjects.values.toList().subList(0, 10)
    }

    suspend fun getObjectDetailsById(objectId: Int): MetObject? {
        return withContext(Dispatchers.IO) {
            return@withContext metMuseumApi.getObjectById(objectId).body()
        }
    }


}