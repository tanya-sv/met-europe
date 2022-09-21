package com.metgallery.ui.departments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.metgallery.util.EventObserver
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentDepartmentsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentsFragment : Fragment() {

    private val viewModel: DepartmentsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentDepartmentsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = FragmentDepartmentsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.rvDepartmentsList.adapter = DepartmentAdapter(viewModel)
        viewDataBinding.rvDepartmentsList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        viewModel.selectedDepartment.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                "departmentId" to it.departmentId,
                "departmentName" to it.displayName
            )
            findNavController().navigate(R.id.action_DepartmentsFragment_to_CollectionFragment, bundle)
        })
    }
}