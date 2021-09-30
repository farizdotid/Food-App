package org.codejudge.application.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.codejudge.application.data.remote.PlaceDataSource
import org.codejudge.application.model.ui.NearbyPlace
import org.codejudge.application.utils.network.Result
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placeDataSource: PlaceDataSource
) {
    /**
     * REQUEST NEARBY PLACES
     */
    suspend fun requestNearbyPlaces(keyword: String?): Flow<Result<List<NearbyPlace>>> {
        return flow {
            emit(Result.loading())
            val result = placeDataSource.requestNearbyPlaces(keyword)
            if (result.status == Result.Status.SUCCESS) {
                val nearbyPlaces = arrayListOf<NearbyPlace>()

                result.data?.results?.forEach { nearbyPlace ->
                    val placeId = nearbyPlace?.placeId ?: "-"
                    val name = nearbyPlace?.name ?: "-"
                    val isOpen = nearbyPlace?.openingHours?.openNow ?: false
                    val photos = nearbyPlace?.photos
                    val initPhotos =
                        if (photos?.isNotEmpty() == true) photos[0]?.photoReference ?: "-" else ""
                    val rating = nearbyPlace?.rating ?: 0.0
                    val vicinity = nearbyPlace?.vicinity ?: "-"

                    nearbyPlaces.add(
                        NearbyPlace(
                            id = placeId, image = initPhotos,
                            storeName = name, storeAddress = vicinity, rating = rating,
                            isOpen = isOpen
                        )
                    )
                }

                emit(Result.success(nearbyPlaces))
            }
        }.flowOn(Dispatchers.IO)
    }
}