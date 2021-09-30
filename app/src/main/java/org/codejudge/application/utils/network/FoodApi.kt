package org.codejudge.application.utils.network

import org.codejudge.application.model.response.RespNearbyPlaces
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    /**
     * NEARBY PLACES
     */
    @GET("json")
    suspend fun requestNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String,
        @Query("keyword") keyword: String?
    ): Response<RespNearbyPlaces>
}