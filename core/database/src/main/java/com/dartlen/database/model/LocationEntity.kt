package com.dartlen.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dartlen.model.LatLng
import com.dartlen.model.Road
import java.util.*

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val duration: Long,
    val max: Double,
    val min: Double,
    val elevation: Double,
    val distance: Float,
    val avrSpeed: Float,
    val startTime: Date,
    val endTime: Date,
    val path: List<List<LatLng>>
)

fun LocationEntity.asExternalModel() = Road(
    id = id,
    duration = duration,
    max = max,
    min = min,
    elevation = elevation,
    distance = distance,
    avrSpeed = avrSpeed,
    startTime = startTime,
    endTime = endTime,
    path = path.map { it.toMutableList() }.toMutableList()
)

fun Road.asEntityModel() = LocationEntity(
    id = id,
    duration = duration,
    max = max,
    min = min,
    elevation = elevation,
    distance = distance,
    avrSpeed = avrSpeed,
    startTime = startTime,
    endTime = endTime,
    path = path
)