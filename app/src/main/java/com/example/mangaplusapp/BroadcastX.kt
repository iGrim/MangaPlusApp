package com.example.mangaplusapp


import com.google.gson.annotations.SerializedName

data class BroadcastX(
    @SerializedName("day")
    val day: String,
    @SerializedName("string")
    val string: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("timezone")
    val timezone: String
)