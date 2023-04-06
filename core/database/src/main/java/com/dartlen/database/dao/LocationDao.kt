package com.dartlen.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.dartlen.database.converter.Converters
import com.dartlen.database.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query(value = "SELECT * FROM location")
    fun getRoadsEntities(): Flow<List<LocationEntity>>

    @Insert
    suspend fun insert(topicEntities: LocationEntity): Long

    @Query(
        value = """
            DELETE FROM location
            WHERE id = :id
        """,
    )
    suspend fun deleteRoad(id: Long)
}