package com.metgallery.ui.collection

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metgallery.data.model.MetCollectionItem
import com.metgallery.ui.databinding.ListItemCollectionBinding

class CollectionAdapter(private val viewModel: CollectionViewModel) :
    ListAdapter<MetCollectionItem, CollectionAdapter.ViewHolder>(CollectionItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(private val binding: ListItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CollectionViewModel, item: MetCollectionItem) {
            binding.viewmodel = viewModel
            binding.item = item
            //binding.ivCollectionImage.layoutParams.width = getNewImageWidth(binding.root)
            binding.executePendingBindings()
        }

        //TODO revise
        private fun getNewImageWidth(itemView: View): Int {
            val displayMetrics = DisplayMetrics()

            (itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getMetrics(displayMetrics)

            val width = displayMetrics.widthPixels
            val horizontalPadding = itemView.paddingStart

            return (width / 2 - horizontalPadding)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCollectionBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class CollectionItemDiffCallback : DiffUtil.ItemCallback<MetCollectionItem>() {

    override fun areItemsTheSame(oldItem: MetCollectionItem, newItem: MetCollectionItem): Boolean {
        return oldItem.objectId == newItem.objectId
    }

    override fun areContentsTheSame(oldItem: MetCollectionItem, newItem: MetCollectionItem): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<MetCollectionItem>) {
    (listView.adapter as CollectionAdapter).submitList(items)
}

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).into(imageView);
}
