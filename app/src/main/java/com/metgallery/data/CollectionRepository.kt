package com.metgallery.data

import android.content.Context
import android.util.Log
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.domain.EuropeanCollectionEra
import com.metgallery.domain.GeoLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val metMuseumApi: MetMuseumApi,
    context: Context
) {

    private val objectsImagesMap: MutableMap<Int, String> = mutableMapOf()

    init {
        context.assets.open("europe_images.csv").bufferedReader().use {
            it.readLines().forEach { line ->
                val pair = line.split(",")
                objectsImagesMap[pair[0].toInt()] = pair[1]
            }
            Log.d("myTest", "we have a map: " + objectsImagesMap.keys.size)
        }
    }

    suspend fun searchEuropeanPaintings(
        geoLocation: GeoLocation?,
        era: EuropeanCollectionEra?
    ): List<MetCollectionItem> {

        return withContext(Dispatchers.IO) {

            val response = metMuseumApi.getSearch(
                departmentId = 11,
                geoLocation = geoLocation?.apiValue,
                dateBegin = era?.dateBegin,
                dateEnd = era?.dateEnd
            ).body()

            //transform to a list of collection items with image url for each object id
            response?.objectIDs?.filter { objectsImagesMap.keys.contains(it) }
                ?.map { MetCollectionItem(it, objectsImagesMap[it]!!) }
                ?: emptyList()
        }
    }


}