package org.codejudge.application.utils.network

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): ErrorApi? {
        val converter =
            retrofit.responseBodyConverter<ErrorApi>(ErrorApi::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            ErrorApi()
        }
    }
}