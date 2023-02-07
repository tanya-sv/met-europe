package com.tsvetova.metgallery.data

import com.nhaarman.mockito_kotlin.mock
import com.tsvetova.metgallery.data.api.MetMuseumApi
import com.tsvetova.metgallery.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Response

@ExperimentalCoroutinesApi
class CollectionRepositoryUnitTest {

    private val api: MetMuseumApi = mock()
    private val dao: MetCollectionDao = mock()

    private val collectionRepository: CollectionRepository = CollectionRepository(api, dao)

    @Test
    fun testSearchAllEuropeanPaintings() = runTest {

        When calling dao.getAll() itReturns TEST_LIST

        val result: Result<List<MetCollectionItem>> =
            collectionRepository.searchEuropeanPaintings(ArtistNationality.None, EuropeanCollectionEra.None)

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (TEST_LIST)
    }

    @Test
    fun testSearchEuropeanPaintingsByArtist() = runTest {

        val byNationality = listOf(item2, item3)
        When calling dao.findByArtistNationality(ArtistNationality.Norwegian.displayValue) itReturns byNationality

        val result: Result<List<MetCollectionItem>> =
            collectionRepository.searchEuropeanPaintings(ArtistNationality.Norwegian, EuropeanCollectionEra.None)

        Verify on dao that dao.findByArtistNationality(ArtistNationality.Norwegian.displayValue)

        result `should be instance of`(Result.Success::class)
        (result as Result.Success).data `should be equal to`(byNationality)
    }

    @Test
    fun testSearchEuropeanPaintingsByEra() = runTest {

        val byEra = listOf(item2, item4)
        When calling dao.findByEra(1800, 1900) itReturns byEra

        val result: Result<List<MetCollectionItem>> =
            collectionRepository.searchEuropeanPaintings(ArtistNationality.None, EuropeanCollectionEra.AD1800_1900)

        Verify on dao that dao.findByEra(1800, 1900)

        result `should be instance of`(Result.Success::class)
        (result as Result.Success).data `should be equal to`(byEra)
    }

    @Test
    fun testSearchEuropeanPaintingsByArtistAndEra() = runTest {

        val byNationalityAndEra = listOf(item6)
        When calling dao.findByArtistNationalityAndEra("Swedish", 1000, 1400) itReturns byNationalityAndEra

        val result: Result<List<MetCollectionItem>> =
            collectionRepository.searchEuropeanPaintings(ArtistNationality.Swedish, EuropeanCollectionEra.AD1000_1400)

        Verify on dao that dao.findByArtistNationalityAndEra("Swedish", 1000, 1400)

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (byNationalityAndEra)
    }


    @Test
    fun testSearchByTag() = runTest {

        When calling dao.getAll() itReturns TEST_LIST

        val result: Result<List<MetCollectionItem>> = collectionRepository.searchByTag("portrait")

        Verify on dao that dao.getAll()

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (listOf(item2, item4, item5))
    }


    @Test
    fun testSearchByArtist() = runTest {

        When calling dao.findByArtist("artist1") itReturns listOf(item1, item3, item5)

        val result: Result<List<MetCollectionItem>> = collectionRepository.searchByArtist("artist1")

        Verify on dao that dao.findByArtist("artist1")

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (listOf(item1, item3, item5))
    }

    @Test
    fun testGetCountBySearchTerm() = runTest {

        When calling dao.getAll() itReturns TEST_LIST

        val result: Result<List<SearchResult>> = collectionRepository.getCountBySearchTerm("art")

        result `should be instance of` (Result.Success::class)
        result as Result.Success

        result.data `should contain` (TagSearchResult("art", 2))
        result.data `should contain` (ArtistSearchResult("artist1", 3))
        result.data `should contain` (ArtistSearchResult("artist2", 2))
        result.data `should contain` (ArtistSearchResult("artist3", 1))
        result.data `should contain` (ArtistSearchResult("artist4", 1))
    }

    @Test
    fun testGetObjectDetailsById() = runTest {

        val metObject = MetObject(objectID = 111, title = "Test title")
        When calling api.getObjectById(111) itReturns Response.success(metObject)

        val result: Result<MetObject?> = collectionRepository.getObjectDetailsById(111)

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (metObject)
    }

    @Test
    fun testGetFavourites() = runTest {
        When calling dao.getAllFavourites() itReturns FAVOURITES

        val result: Result<List<MetCollectionItem>> = collectionRepository.getFavourites()

        result `should be instance of` (Result.Success::class)
        (result as Result.Success).data `should be equal to` (FAVOURITES)
    }

    @Test
    fun testUpdateFavouriteTrue() = runTest {

        val testItem = MetCollectionFavourite(123, true)
        collectionRepository.updateFavourite(testItem)

        Verify on dao that dao.update(testItem)
    }

    @Test
    fun testUpdateFavouriteFalse() = runTest {

        val testItem = MetCollectionFavourite(321, false)
        collectionRepository.updateFavourite(testItem)

        Verify on dao that dao.update(testItem)
    }

    companion object TestData {

        val item1 = MetCollectionItem(11, "title", "artist1", "American", 1440, 1440,
            "medium", "classification", listOf("tree", "animals"), "image_url", 100f, 100f, false
        )
        val item2 = MetCollectionItem( 22,"title", "artist2", "Norwegian", 1833, 1836,
            "medium", "classification", listOf("men", "portrait"), "image_url", 100f, 100f, false
        )
        val item3 = MetCollectionItem(33, "title", "artist1", "Norwegian", 1800, 1902,
            "medium", "classification", listOf("women", "fruit", "apple"), "image_url", 100f, 100f, false
        )
        val item4 = MetCollectionItem(44, "title", "artist3", "German", 1811, 1899,
            "medium", "classification", listOf("women", "portrait"), "image_url", 100f, 100f, false
        )
        val item5 = MetCollectionItem(55, "title", "artist1", "Austrian", 1700, 1800,
            "medium", "classification", listOf("royal", "women", "portrait", "art"), "image_url", 100f, 100f, false
        )
        val item6 = MetCollectionItem(66, "title", "artist2", "Swedish", 1223, 1223,
            "medium", "classification", listOf("tree", "landscape", "nature"), "image_url", 100f, 100f, false
        )
        val item7 = MetCollectionItem(66, "title", "artist4", "Swedish", 1504, 1505,
            "medium", "classification", listOf("still life", "fruit", "art"), "image_url", 100f, 100f, false
        )

        val TEST_LIST: List<MetCollectionItem> = listOf(item1, item2, item3, item4, item5, item6, item7)

        val FAVOURITES: List<MetCollectionItem> = listOf(
            MetCollectionItem(
                11,
                "title",
                "artist",
                "nationality",
                1,
                2,
                "medium",
                "classification",
                listOf("tag1", "tag2"),
                "image_url",
                100f,
                100f,
                true
            ),
            MetCollectionItem(
                11,
                "title",
                "artist",
                "nationality",
                1,
                2,
                "medium",
                "classification",
                listOf("tag1", "tag2"),
                "image_url",
                100f,
                100f,
                true
            ),
            MetCollectionItem(
                11,
                "title",
                "artist",
                "nationality",
                1,
                2,
                "medium",
                "classification",
                listOf("tag1", "tag2"),
                "image_url",
                100f,
                100f,
                true
            )
        )
    }


}