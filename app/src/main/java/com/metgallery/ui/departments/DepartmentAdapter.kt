package com.metgallery.ui.departments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metgallery.data.api.model.MetDepartment
import com.metgallery.ui.databinding.ListItemDepartmentBinding

class DepartmentAdapter(private val viewModel: DepartmentsViewModel) :
    ListAdapter<MetDepartment, DepartmentAdapter.ViewHolder>(DepartmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(val binding: ListItemDepartmentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: DepartmentsViewModel, item: MetDepartment) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDepartmentBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class DepartmentDiffCallback : DiffUtil.ItemCallback<MetDepartment>() {

    override fun areItemsTheSame(oldItem: MetDepartment, newItem: MetDepartment): Boolean {
        return oldItem.departmentId == newItem.departmentId
    }

    override fun areContentsTheSame(oldItem: MetDepartment, newItem: MetDepartment): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<MetDepartment>) {
    (listView.adapter as DepartmentAdapter).submitList(items)
}
