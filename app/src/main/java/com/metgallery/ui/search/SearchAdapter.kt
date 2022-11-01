package com.metgallery.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metgallery.data.model.SearchTag
import com.metgallery.ui.databinding.ListItemSearchCountBinding

class SearchAdapter(private val viewModel: SearchViewModel) :
    ListAdapter<SearchTag, SearchAdapter.ViewHolder>(SearchCountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(private val binding: ListItemSearchCountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SearchViewModel, item: SearchTag) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSearchCountBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class SearchCountDiffCallback : DiffUtil.ItemCallback<SearchTag>() {

    override fun areItemsTheSame(oldItem: SearchTag, newItem: SearchTag): Boolean {
        return oldItem.tag == newItem.tag && oldItem.count == newItem.count
    }

    override fun areContentsTheSame(oldItem: SearchTag, newItem: SearchTag): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<SearchTag>) {
    (listView.adapter as SearchAdapter).submitList(items)
}