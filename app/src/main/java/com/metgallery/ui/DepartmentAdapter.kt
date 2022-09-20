package com.metgallery.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.metgallery.data.api.model.MetDepartment

class DepartmentAdapter(
    private val departments: List<MetDepartment>,
    private val clickListener: (department: MetDepartment) -> Unit
) :
    RecyclerView.Adapter<DepartmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_department, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = departments[position].displayName
        holder.itemView.setOnClickListener {
            clickListener(departments[position])
        }
    }

    override fun getItemCount(): Int {
        return departments.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.tvDepartmentName)
        }
    }

}