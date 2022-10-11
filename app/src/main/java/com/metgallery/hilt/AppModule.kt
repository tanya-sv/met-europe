package com.metgallery.hilt

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.metgallery.data.AppDatabase
import com.metgallery.data.CollectionRepository
import com.metgallery.data.MetCollectionDao
import com.metgallery.data.api.MetMuseumApi
import com.metgallery.util.getCollectionItemsFromCsvFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
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
        metCollectionDao: MetCollectionDao
    ): CollectionRepository {
        return CollectionRepository(museumApi, metCollectionDao)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<MetCollectionDao>
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "met-gallery-example.db"
        ).addCallback(object : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                CoroutineScope(Dispatchers.IO).launch {
                    //prepopulate, this is only called once when the db is created for the first time
                    val items = getCollectionItemsFromCsvFile(context)
                    provider.get().insertAll(items)
                }
            }

        }).build()

    @Singleton
    @Provides
    fun provideMetCollectionDao(db: AppDatabase): MetCollectionDao = db.metCollectionDao()

}