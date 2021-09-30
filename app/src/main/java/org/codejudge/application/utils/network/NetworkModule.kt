package org.codejudge.application.utils.network

import android.content.Context
import android.os.Looper
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.codejudge.application.BuildConfig
import org.codejudge.application.di.DaggerSet
import org.codejudge.application.utils.network.converter.SingleToArrayAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@InstallIn(ApplicationComponent::class)
@Module(includes = [IcApiModule::class, InterceptorModule::class])
object NetworkModule {
    private const val FOOD_APP_API_BASE_URL = "${BuildConfig.BASE_URL}/maps/api/place/nearbysearch/"

    @Provides
    @Named(FOOD_APP_API_BASE_URL)
    fun provideBaseUrl(): String {
        return FOOD_APP_API_BASE_URL
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    @Provides
    @Singleton
    fun provideChunkInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    // Use newBuilder() to customize so that thread-pool and connection-pool same are used
    @Provides
    fun provideOkHttpClientBuilder(
        @InternalApi okHttpClient: Lazy<OkHttpClient>
    ): OkHttpClient.Builder {
        return okHttpClient.get().newBuilder()
    }

    @InternalApi
    @Provides
    @Singleton
    fun provideBaseOkHttpClient(
        intereptor: DaggerSet<Interceptor>,
        cache: Cache
    ): OkHttpClient {
        check(Looper.myLooper() != Looper.getMainLooper()) { "HTTP client initialized on main thread" }
        val builder = unSafeOkHttpClient()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(35, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        builder.interceptors().addAll(intereptor)
        builder.cache(cache)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideCache(context: Context): Cache {
        check(Looper.myLooper() != Looper.getMainLooper()) { "Chace initialized on main thread" }
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDir = context.cacheDir
        return Cache(cacheDir, cacheSize.toLong())
    }

    @Singleton
    @Provides
    fun provideMoshiAdapter(): Moshi = Moshi.Builder().add(SingleToArrayAdapter.INSTANCE).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        @InternalApi
        okHttpClient: Lazy<OkHttpClient>,
        @Named(FOOD_APP_API_BASE_URL) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .callFactoryExt { okHttpClient.get().newCall(it) }
            .build()
    }

    fun unSafeOkHttpClient() : OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts:  Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?){}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate>  = arrayOf()
            })

            // Install the all-trusting trust manager
            val  sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() &&  trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
                okHttpClient.hostnameVerifier(HostnameVerifier { _: String?, _: SSLSession? -> true })
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }
}