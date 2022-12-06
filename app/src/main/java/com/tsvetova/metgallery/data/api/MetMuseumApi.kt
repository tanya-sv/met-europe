package com.tsvetova.metgallery.data.api

import com.tsvetova.metgallery.data.model.MetObject
import com.tsvetova.metgallery.data.model.MetObjectsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//"https://collectionapi.metmuseum.org/"

interface MetMuseumApi {

    @GET("/public/collection/v1/objects")
    suspend fun getObjectsByDepartmentId(@Query("departmentIds") departmentId: Int): Response<MetObjectsResponse>

    @GET("/public/collection/v1/objects/{objectId}")
    suspend fun getObjectById(@Path("objectId") objectId: Int): Response<MetObject>

    @GET("/public/collection/v1/search")
    suspend fun getSearch(
        @Query("departmentIds") departmentId: Int,
        @Query("hasImages") hasImages: Boolean = true,
        @Query("q") searchTerm: String = "",
        @Query("geoLocation") geoLocation: String?,
        @Query ("dateBegin") dateBegin: Int?,
        @Query ("dateEnd") dateEnd: Int?
    ): Response<MetObjectsResponse>

}