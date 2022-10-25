package com.metgallery.util

import android.content.Context
import com.metgallery.data.model.MetCollectionItem
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.QuoteMode

fun getCollectionItemsFromCsvFile(context: Context): List<MetCollectionItem> {
    val metObjects = mutableListOf<MetCollectionItem>()

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
                        classification = elements[9],
                        tags = elements[10].split("|"),
                        imageUrl = elements[11],
                        height = elements[12].toFloat(),
                        width = elements[13].toFloat(),
                        false
                    )
                )
            }
    }

    return metObjects
}
