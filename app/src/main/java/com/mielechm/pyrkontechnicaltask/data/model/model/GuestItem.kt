package com.mielechm.pyrkontechnicaltask.data.model.model

import com.google.gson.annotations.SerializedName

data class GuestItem(
    val name: String,
    val summary: String,
    @SerializedName("imageURL")
    val imageUrl: String,
    val zones: List<String>
)
