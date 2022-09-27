package com.metgallery.data.api

import com.metgallery.data.api.model.MetObject
import com.metgallery.data.api.model.MetObjectsResponse
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
        @Query("q") searchTerm: String = "",
        @Query("departmentId") departmentId: Int,
        @Query("hasImages") hasImages: Boolean = true,
        @Query("geoLocation") geoLocation: String?,
        @Query ("dateBegin") dateBegin: Int?,
        @Query ("dateEnd") dateEnd: Int?
    ): Response<MetObjectsResponse>

}