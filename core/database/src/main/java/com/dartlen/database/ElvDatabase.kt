package com.dartlen.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dartlen.database.converter.Converters
import com.dartlen.database.converter.PolyLineConverter
import com.dartlen.database.dao.LocationDao
import com.dartlen.database.model.LocationEntity

@Database(
    entities = [LocationEntity::class], version = 1, exportSchema = false
)
@TypeConverters(*[Converters::class, PolyLineConverter::class])
abstract class ElvDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}