package com.metgallery.hilt

import android.content.Context
import androidx.room.Room
import com.metgallery.data.AppDatabase
import com.metgallery.data.CollectionRepository
import com.metgallery.data.MetCollectionDao
import com.metgallery.data.api.MetMuseumApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMetMuseumApi(okHttpClient: OkHttpClient): MetMuseumApi {
        return Retrofit.Builder().baseUrl("https://collectionapi.metmuseum.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MetMuseumApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpBuilder.addInterceptor(interceptor)

        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideCollectionRepository(
        museumApi: MetMuseumApi,
        metCollectionDao: MetCollectionDao,
        @ApplicationContext context: Context
    ): CollectionRepository {
        return CollectionRepository(museumApi, metCollectionDao, context)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "met-gallery-example"
        ).build()

    @Singleton
    @Provides
    fun provideMetCollectionDao(db: AppDatabase): MetCollectionDao = db.metCollectionDao()

}