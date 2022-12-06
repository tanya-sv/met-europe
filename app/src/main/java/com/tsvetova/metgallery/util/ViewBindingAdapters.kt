package com.tsvetova.metgallery.util

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jsibbold.zoomage.ZoomageView

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).into(imageView)
}

interface ImageLoadingCallback {
    fun onLoading()
    fun onSuccess()
    fun onError()
}

@BindingAdapter(value = ["app:largeImageUrl", "app:callback"], requireAll = false)
fun setLargeImageUrl(photoView: ZoomageView, imageUrl: String, callback: ImageLoadingCallback) {
    if (imageUrl.isNotBlank()) {
        callback.onLoading()

        Glide.with(photoView)
            .load(imageUrl)
            .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    ex: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", ex?.message ?: "Error downloading $imageUrl")
                    callback.onError()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    lisFirstResource: Boolean
                ): Boolean {
                    callback.onSuccess()
                    return false
                }
            })
            .into(photoView)
    }
}