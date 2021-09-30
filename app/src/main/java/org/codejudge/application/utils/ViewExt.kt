package org.codejudge.application.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.codejudge.application.utils.image.GlideApp

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun ImageView.loadUrlCenterCropRadius(source: Any, radius: Int) {
    GlideApp.with(this)
        .load(source)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
}