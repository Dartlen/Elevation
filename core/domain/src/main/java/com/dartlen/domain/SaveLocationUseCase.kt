package com.dartlen.domain

import com.dartlen.data.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(val locationRepository: LocationRepository) {
}