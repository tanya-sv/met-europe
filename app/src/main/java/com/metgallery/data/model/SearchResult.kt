package com.metgallery.data.model

sealed class SearchResult(val term: String, val resultCount: Int, val argumentName: String)

data class TagSearchResult(val tag: String, val count: Int) : SearchResult(tag, count, "tag")
data class ArtistSearchResult(val artist: String, val count: Int) : SearchResult(artist, count, "artist")