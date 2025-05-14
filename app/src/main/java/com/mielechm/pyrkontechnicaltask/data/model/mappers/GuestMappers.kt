package com.mielechm.pyrkontechnicaltask.data.model.mappers

import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity
import com.mielechm.pyrkontechnicaltask.data.model.model.GuestItem

fun GuestItem.toGuestEntity() = GuestEntity(
    name = this.name,
    summary = this.summary,
    imageUrl = this.imageUrl,
    zones = this.zones
)

fun GuestEntity.toGuestItem() = GuestItem(
    name = this.name,
    summary = this.summary,
    imageUrl = this.imageUrl,
    zones = this.zones
)