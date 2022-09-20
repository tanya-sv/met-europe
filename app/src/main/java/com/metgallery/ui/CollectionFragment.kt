package com.metgallery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.metgallery.domain.GetCollectionByQuery
import com.metgallery.ui.databinding.FragmentCollectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var getCollectionByQuery: GetCollectionByQuery

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val departmentId = arguments?.getInt("departmentId")
        val departmentName = arguments?.getString("departmentName")

        (activity as AppCompatActivity?)?.supportActionBar?.title = departmentName

        departmentId?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = getCollectionByQuery(it)

                binding.rvCollection.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                binding.rvCollection.adapter = CollectionAdapter(result) { item ->
                    val bundle = bundleOf(
                        "itemDetails" to item
                    )
                    findNavController().navigate(R.id.action_CollectionFragment_to_ItemDetailsFragment, bundle)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}