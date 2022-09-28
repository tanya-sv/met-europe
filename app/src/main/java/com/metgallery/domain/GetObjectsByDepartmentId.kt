package com.metgallery.domain

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.model.MetObjectsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetObjectsByDepartmentId {
    suspend operator fun invoke(departmentId: Int): MetObjectsResponse
}

class GetObjectsByDepartmentIdImpl @Inject constructor(private val metMuseumApi: MetMuseumApi) :
    GetObjectsByDepartmentId {

    override suspend operator fun invoke(departmentId: Int): MetObjectsResponse {
        return withContext(Dispatchers.IO) {
            ///TODO log error?
            return@withContext metMuseumApi.getObjectsByDepartmentId(departmentId).body()
                ?: MetObjectsResponse(0, emptyList())
        }
    }
}
