package org.codejudge.application.utils.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorApi(
    val error: String? = null,
    val error_description: String? = null,
    val message: String? = null
)