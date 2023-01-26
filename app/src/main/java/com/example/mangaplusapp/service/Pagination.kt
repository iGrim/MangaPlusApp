package com.example.mangaplusapp.service


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("has_next_page")
    val hasNextPage: Boolean,
    @SerializedName("items")
    val items: Items,
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int
)