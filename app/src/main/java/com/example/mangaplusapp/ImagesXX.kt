package com.example.mangaplusapp


import com.google.gson.annotations.SerializedName

data class ImagesXX(
    @SerializedName("jpg")
    val jpg: JpgX,
    @SerializedName("webp")
    val webp: WebpX
)