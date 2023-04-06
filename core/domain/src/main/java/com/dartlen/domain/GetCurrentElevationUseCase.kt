package com.dartlen.domain

import com.dartlen.model.Elevation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentElevationUseCase @Inject constructor() {
    fun getElevation(): Flow<Elevation> = flow {
        emit(Elevation(0))
    }
}