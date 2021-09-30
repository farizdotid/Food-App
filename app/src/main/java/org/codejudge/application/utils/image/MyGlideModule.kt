package org.codejudge.application.utils.image

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import org.codejudge.application.utils.network.NetworkModule
import java.io.InputStream

@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val unsafeHttpClient = NetworkModule.unSafeOkHttpClient().build()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(unsafeHttpClient)
        )
    }
}