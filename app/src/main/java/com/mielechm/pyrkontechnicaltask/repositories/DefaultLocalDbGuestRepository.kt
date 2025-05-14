package com.mielechm.pyrkontechnicaltask.repositories

import com.mielechm.pyrkontechnicaltask.data.model.PyrkonTechTaskDao
import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity

class DefaultLocalDbGuestRepository(private val dao: PyrkonTechTaskDao) : LocalDbGuestRepository {
    override suspend fun upsertGuestEntity(guestEntity: GuestEntity) {
        dao.upsertGuestEntity(guestEntity)
    }

    override suspend fun getGuestEntityByName(name: String): GuestEntity =
        dao.getGuestEntityByName(name)
}