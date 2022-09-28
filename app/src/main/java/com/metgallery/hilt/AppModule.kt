package com.metgallery.hilt

import android.content.Context
import com.metgallery.data.CollectionRepository
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
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
    fun provideCollectionRepository(
        museumApi: MetMuseumApi,
        @ApplicationContext context: Context
    ): CollectionRepository {
        return CollectionRepository(museumApi, context)
    }

}