package com.tsvetova.metgallery.ui.collection

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsvetova.metgallery.data.model.MetCollectionItem
import com.tsvetova.metgallery.ui.databinding.ListItemCollectionBinding

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

            //setting fixed size is required for Glide to work properly with StaggeredGridLayoutManager
            getImageViewSize(binding.root, item).let {
                binding.ivCollectionImage.layoutParams.width = it.first
                binding.ivCollectionImage.layoutParams.height = it.second
            }

            binding.executePendingBindings()
        }

        private fun getImageViewSize(itemView: View, item: MetCollectionItem): Pair<Int, Int> {
            val displayMetrics = DisplayMetrics()

            (itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getMetrics(displayMetrics)

            val width = displayMetrics.widthPixels
            val horizontalPadding = itemView.paddingStart + itemView.paddingEnd

            val newWidth = width / 2 - horizontalPadding
            //keep the aspect ratio of the item
            val newHeight = (newWidth * item.height) / item.width

            return Pair(newWidth, newHeight.toInt())
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

