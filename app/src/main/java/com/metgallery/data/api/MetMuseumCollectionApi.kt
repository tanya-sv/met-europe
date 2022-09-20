package com.metgallery.data.api

import com.metgallery.data.api.model.MetCollectionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//"https://metmuseum.org/api/collection/collectionlisting/"

interface MetMuseumCollectionApi {

    @Headers(
        "Accept: application/json",
        "User-Agent: Test Met Gallery"
    )
    @GET("/api/collection/collectionlisting")
    suspend fun getCollectionByQuery(
        @Query("q") searchTerm: String = "",
        @Query("era") era: String = "",
        @Query("department") departmentId: Int,
        @Query("offset") offset: Int = 0,
        @Query("showOnly") showOnly: String = "openAccess"
    ): Response<MetCollectionResponse>
}