package com.metgallery.domain

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.api.model.MetObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetObjectDetailsById {
    suspend operator fun invoke(objectId: Int): MetObject?
}

class GetObjectDetailsByIdImpl @Inject constructor(private val metMuseumApi: MetMuseumApi) : GetObjectDetailsById {

    override suspend operator fun invoke(objectId: Int): MetObject? {
        return withContext(Dispatchers.IO) {
            ///TODO log error?
            return@withContext metMuseumApi.getObjectById(objectId).body()
        }
    }
}


