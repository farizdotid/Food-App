package org.codejudge.application.utils.network

import okhttp3.Call
import okhttp3.Request
import retrofit2.Retrofit

@PublishedApi
internal inline fun Retrofit.Builder.callFactoryExt(
    crossinline body: (Request) -> Call
) = callFactory { request -> body(request) }