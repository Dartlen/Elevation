package com.dartlen.model

import java.util.*

data class Road(
    val id: Long,
    val duration: Long,
    val max: Double,
    val min: Double,
    val elevation: Double,
    val distance: Float,
    val avrSpeed: Float,
    val startTime: Date,
    val endTime: Date,
    val path: Polylines
)
