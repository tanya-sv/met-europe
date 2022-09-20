package com.metgallery.hilt

import com.metgallery.data.api.MetMuseumApi
import com.metgallery.data.api.MetMuseumCollectionApi
import com.metgallery.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMetMuseumCollectionApi(okHttpClient: OkHttpClient): MetMuseumCollectionApi {

        return Retrofit.Builder().baseUrl("https://metmuseum.org/api/collection/collectionlisting/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MetMuseumCollectionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMetMuseumApi(okHttpClient: OkHttpClient): MetMuseumApi {

        return Retrofit.Builder().baseUrl("https://collectionapi.metmuseum.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MetMuseumApi::class.java)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpBuilder.addInterceptor(interceptor)

        return okHttpBuilder.build()
    }

    @Provides
    fun provideGetDepartments(museumApi: MetMuseumApi) : GetDepartments {
        return GetDepartmentsImpl(museumApi)
    }

    @Provides
    fun provideGetObjectDetailsById(museumApi: MetMuseumApi) : GetObjectDetailsById {
        return GetObjectDetailsByIdImpl(museumApi)
    }

    @Provides
    fun provideGetCollectionByQuery(museumCollectionApi: MetMuseumCollectionApi) : GetCollectionByQuery {
        return GetCollectionByQueryImpl(museumCollectionApi)
    }

}