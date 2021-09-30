package org.codejudge.application.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RespNearbyPlaces(
    @Json(name = "results")
    val results: List<Result?>? = null
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "business_status")
        val businessStatus: String? = null,
        @Json(name = "icon")
        val icon: String? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "opening_hours")
        val openingHours: OpeningHours? = null,
        @Json(name = "photos")
        val photos: List<Photo?>? = null,
        @Json(name = "place_id")
        val placeId: String? = null,
        @Json(name = "rating")
        val rating: Double? = null,
        @Json(name = "reference")
        val reference: String? = null,
        @Json(name = "scope")
        val scope: String? = null,
        @Json(name = "user_ratings_total")
        val userRatingsTotal: Int? = null,
        @Json(name = "vicinity")
        val vicinity: String? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class OpeningHours(
            @Json(name = "open_now")
            val openNow: Boolean? = null
        )

        @JsonClass(generateAdapter = true)
        data class Photo(
            @Json(name = "height")
            val height: Int? = null,
            @Json(name = "html_attributions")
            val htmlAttributions: List<String?>? = null,
            @Json(name = "photo_reference")
            val photoReference: String? = null,
            @Json(name = "width")
            val width: Int? = null
        )
    }
}