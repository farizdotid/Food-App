package org.codejudge.application.data.remote

import org.codejudge.application.BuildConfig
import org.codejudge.application.model.response.RespNearbyPlaces
import org.codejudge.application.utils.Constant
import org.codejudge.application.utils.network.ErrorUtils
import org.codejudge.application.utils.network.FoodApi
import org.codejudge.application.utils.network.Result
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class PlaceDataSource @Inject constructor(
    private val retrofit: Retrofit
) {
    /**
     * REQUEST NEARBY PLACES
     */
    suspend fun requestNearbyPlaces(keyword: String?): Result<RespNearbyPlaces> {
        val apiService = retrofit.create(FoodApi::class.java)
        return getResponse {
            apiService.requestNearbyPlaces(
                location = Constant.STATIC_LOCATION_COORDINATE,
                radius = 2500, type = "restaurant", key = BuildConfig.API_KEY_MAPS,
                keyword = keyword
            )
        }
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>
    ): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(
                    errorResponse?.error_description ?: errorResponse?.message.toString(),
                    errorResponse
                )
            }
        } catch (e: Throwable) {
            Result.error("Error ${e.message}", null)
        }
    }

}