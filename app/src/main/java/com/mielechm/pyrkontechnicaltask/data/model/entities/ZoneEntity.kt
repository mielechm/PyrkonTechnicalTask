package com.mielechm.pyrkontechnicaltask.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("zone")
data class ZoneEntity(
    @PrimaryKey(true)
    val id: Int,
    val name: String
)
