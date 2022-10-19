package com.metgallery.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).into(imageView);
}

@BindingAdapter(value = ["app:largeImageUrl", "app:progressBar"], requireAll = false)
fun setLargeImageUrl(photoView: PhotoView, imageUrl: String, progressBar: ProgressBar) {
    Glide.with(photoView).load(imageUrl)
        //TODO use placeholder?
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                //TODO react to error
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                lisFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }
        })
        .into(photoView)
}