package com.metgallery.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentCollectionBinding
import com.metgallery.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentCollectionBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.rvCollection.adapter = CollectionAdapter(viewModel)

        val departmentId = arguments?.getInt("departmentId")
        val departmentName = arguments?.getString("departmentName")

        (activity as AppCompatActivity?)?.supportActionBar?.title = departmentName

        departmentId?.let {
            viewModel.loadCollection(it)
        }

        viewModel.selectedItem.observe(this.viewLifecycleOwner, EventObserver {

            val objectId = it.url
                .substringAfter("/art/collection/search/")
                .substringBefore("?").toInt()

            val bundle = bundleOf(
                "objectId" to objectId
            )
            findNavController().navigate(R.id.action_CollectionFragment_to_ItemDetailsFragment, bundle)
        })
    }

}