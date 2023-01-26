package com.example.mangaplusapp.service


import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
)