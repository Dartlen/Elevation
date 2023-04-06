package com.dartlen.data

import com.dartlen.model.Road
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun insertRoad(road: Road): Long
    suspend fun deleteRoad(id: Long)
    fun getRoadsEntities(): Flow<List<Road>>

}