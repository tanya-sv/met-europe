package com.metgallery.ui

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metgallery.data.api.model.MetCollectionItem
import com.metgallery.data.api.model.MetDepartment

class CollectionAdapter(
    private val collection: List<MetCollectionItem>,
    private val clickListener: (department: MetCollectionItem) -> Unit
) :
    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView

        init {
            imageView = view.findViewById(R.id.ivCollectionImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_collection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.layoutParams.width = getNewImageWidth(holder)

        Glide.with(holder.itemView).load(collection[position].image).into(holder.imageView);

        holder.itemView.setOnClickListener {
            clickListener(collection[position])
        }
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    private fun getNewImageWidth(holder: ViewHolder): Int {
        val displayMetrics = DisplayMetrics()

        (holder.itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val horizontalPadding = holder.itemView.paddingStart

        return (width / 2 - horizontalPadding)
    }

}