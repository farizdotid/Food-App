package org.codejudge.application.utils

import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.codejudge.application.BuildConfig

/**
 * SNACKBAR
 */
inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

/**
 * Show a snackbar with [message]
 */
fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) = snack(message, length) {}

fun String.generatePhotoPlace(): String {
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$this&key=${BuildConfig.API_KEY_MAPS}"
}