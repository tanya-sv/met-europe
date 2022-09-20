package com.metgallery.domain

import com.metgallery.data.api.MetMuseumCollectionApi
import com.metgallery.data.api.model.MetCollectionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCollectionByQuery {
    suspend operator fun invoke(departmentId: Int): List<MetCollectionItem>
}

class GetCollectionByQueryImpl @Inject constructor(private val metMuseumCollectionApi: MetMuseumCollectionApi) :
    GetCollectionByQuery {

    override suspend fun invoke(departmentId: Int): List<MetCollectionItem> {
        return withContext(Dispatchers.IO) {
            //TODO extract objectId from url
            val result = metMuseumCollectionApi.getCollectionByQuery(departmentId = departmentId).body()?.results
            return@withContext result ?: emptyList()
        }
    }
}

