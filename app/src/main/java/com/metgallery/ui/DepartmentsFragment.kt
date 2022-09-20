package com.metgallery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.metgallery.domain.GetDepartments
import com.metgallery.ui.databinding.FragmentDepartmentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DepartmentsFragment : Fragment() {

    private var _binding: FragmentDepartmentsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var getDepartments: GetDepartments

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDepartmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val departments = getDepartments().sortedBy { it.displayName }

            binding.rvDepartmentsList.layoutManager = LinearLayoutManager(context)
            binding.rvDepartmentsList.adapter = DepartmentAdapter(departments) { department ->
                val bundle = bundleOf(
                    "departmentId" to department.departmentId,
                    "departmentName" to department.displayName
                )
                findNavController().navigate(R.id.action_DepartmentsFragment_to_CollectionFragment, bundle)
            }
            binding.rvDepartmentsList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}