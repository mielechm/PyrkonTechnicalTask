package com.mielechm.pyrkontechnicaltask.data.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mielechm.pyrkontechnicaltask.data.model.converters.StringListTypeConverter
import com.mielechm.pyrkontechnicaltask.data.model.entities.GuestEntity
import com.mielechm.pyrkontechnicaltask.data.model.entities.ZoneEntity

@Database(entities = [GuestEntity::class, ZoneEntity::class], exportSchema = false, version = 1)
@TypeConverters(value = [StringListTypeConverter::class])
abstract class PyrkonTechTaskDatabase : RoomDatabase() {
    abstract fun pyrkonTechTaskDao(): PyrkonTechTaskDao

    companion object {
        val DATABASE_NAME = "pyrkon_db"
    }
}