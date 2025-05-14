package com.mielechm.pyrkontechnicaltask.data.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity

@Dao
interface PyrkonTechTaskDao {

    @Upsert
    suspend fun upsertGuestEntity(guestEntity: GuestEntity)

    @Query("SELECT * FROM guest WHERE name=:name")
    suspend fun getGuestEntityByName(name: String): GuestEntity

}