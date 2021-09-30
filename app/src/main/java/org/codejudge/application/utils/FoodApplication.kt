package org.codejudge.application.utils

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import org.codejudge.application.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class FoodApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}