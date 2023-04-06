package com.dartlen.database.converter

import androidx.room.TypeConverter
import com.dartlen.model.LatLng
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString

class PolyLineConverter {
    @TypeConverter
    fun fromObject(value: String): List<List<LatLng>>{
        return decodeFromString(ListSerializer(ListSerializer(LatLng.serializer())), value)
    }

    @TypeConverter
    fun polyLinesToString(polylines: List<List<LatLng>>): String{
        return encodeToString(ListSerializer(ListSerializer(LatLng.serializer())), polylines)
    }
}