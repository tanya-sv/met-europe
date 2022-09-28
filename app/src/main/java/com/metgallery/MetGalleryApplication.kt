package com.metgallery

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MetGalleryApplication : Application()  {

    override fun onCreate() {
        super.onCreate()
        //if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

       /* assets.open("europe_images.csv").bufferedReader().use {
            val result = it.readLines()

            val  imagesMap = HashMap<Int, String>()

            result.forEach {
                val pair = it.split(",")
                imagesMap[pair[0].toInt()] = pair[1]
            }
        }*/
    }
}