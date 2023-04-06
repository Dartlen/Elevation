package com.dartlen.domain

import com.dartlen.data.LocationRepository
import com.dartlen.model.Road
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
class GetRoadsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<List<Road>> = locationRepository.getRoadsEntities().map {
        it
    }
}

//private fun Flow<List<NewsResource>>.mapToUserNewsResources(
//    userDataStream: Flow<UserData>,
//): Flow<List<UserNewsResource>> =
//    filterNot { it.isEmpty() }
//        .combine(userDataStream) { newsResources, userData ->
//            newsResources.mapToUserNewsResources(userData)
//        }