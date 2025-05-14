package com.mielechm.pyrkontechnicaltask.repositories

import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity

interface LocalDbGuestRepository {

    suspend fun upsertGuestEntity(guestEntity: GuestEntity)

    suspend fun getGuestEntityByName(name: String): GuestEntity
}