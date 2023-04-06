package com.dartlen.data

import com.dartlen.database.dao.LocationDao
import com.dartlen.database.model.asEntityModel
import com.dartlen.database.model.asExternalModel
import com.dartlen.model.Road
//import com.dartlen.mylibrary.MessageDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImplLocationRepository @Inject constructor(//private val messageDataStore: MessageDataStore,
                                                 private val locationDao: LocationDao) : LocationRepository {

    override suspend fun insertRoad(road: Road): Long {
        return locationDao.insert(road.asEntityModel())
    }

    override suspend fun deleteRoad(id: Long) {
        locationDao.deleteRoad(id)
    }

    override fun getRoadsEntities(): Flow<List<Road>> {
        return locationDao.getRoadsEntities().map { it.map { it.asExternalModel() } }
    }

    //fun sendCommand(message: String) = messageDataStore.sendMessage(message)
}