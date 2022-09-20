package com.metgallery.domain

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.api.model.MetDepartment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetDepartments {
    suspend operator fun invoke(): List<MetDepartment>
}

class GetDepartmentsImpl @Inject constructor(private val metMuseumApi: MetMuseumApi) : GetDepartments {

    override suspend operator fun invoke(): List<MetDepartment> {
        return withContext(Dispatchers.IO) {
            ///TODO log error?
            return@withContext metMuseumApi.getDepartments().body()?.departments ?: emptyList()
        }
    }
}
