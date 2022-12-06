package com.tsvetova.metgallery.ui.item_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsvetova.metgallery.data.model.Tag
import com.tsvetova.metgallery.ui.databinding.ListItemTagBinding

class TagsAdapter(private val viewModel: ItemDetailsViewModel) :
    ListAdapter<Tag, TagsAdapter.ViewHolder>(TagDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(private val binding: ListItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemDetailsViewModel, item: Tag) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTagBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TagDiffCallback : DiffUtil.ItemCallback<Tag>() {

    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.term == newItem.term
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Tag>) {
    (listView.adapter as TagsAdapter).submitList(items)
}