package com.dartlen.model

import kotlinx.serialization.Serializable

typealias Polyline = MutableList<LatLng>

typealias Polylines = MutableList<Polyline>

@Serializable
data class LatLng(val latitude:Double,
             val longitude:Double,
             val elevation: Double)

