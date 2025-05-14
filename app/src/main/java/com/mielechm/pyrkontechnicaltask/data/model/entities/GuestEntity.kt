package com.mielechm.pyrkontechnicaltask.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("guest")
data class GuestEntity(
    @PrimaryKey
    val name: String,
    val summary: String,
    val imageUrl: String,
    val zones: List<String>
)
