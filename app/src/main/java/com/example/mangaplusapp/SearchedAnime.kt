package com.example.mangaplusapp


import com.google.gson.annotations.SerializedName

data class SearchedAnime(
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("request_cache_expiry")
    val `requestCacheExpiry`: Int,
    @SerializedName("request_cached")
    val `requestCached`: Boolean,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: PaginationX,
    @SerializedName("request_hash")
    val `requestHash`: String,
    @SerializedName("results")
    val `results`: List<Result>,
)