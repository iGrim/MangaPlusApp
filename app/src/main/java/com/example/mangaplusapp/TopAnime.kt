package com.example.mangaplusapp


import com.google.gson.annotations.SerializedName

data class TopAnime(
    @SerializedName("data")
    val `data`: List<Result>,
    @SerializedName("links")
    val links: Links,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("top")
    val top: List<Result>
)