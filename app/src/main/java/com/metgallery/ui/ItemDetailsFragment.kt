package com.metgallery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.metgallery.data.api.model.MetCollectionItem
import com.metgallery.domain.GetObjectDetailsById
import com.metgallery.ui.databinding.FragmentItemDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private var _binding: FragmentItemDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var getObjectDetailsById: GetObjectDetailsById

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<MetCollectionItem>("itemDetails")

        item?.let {

            Glide.with(this).load(item.image).into(binding.ivItemDetailsImage)

            val extractedObjectId =
                item.url.substringAfter("/art/collection/search/").substringBefore("?")

            viewLifecycleOwner.lifecycleScope.launch {

                val itemDetails = getObjectDetailsById(extractedObjectId.toInt())

                itemDetails?.let {
                    binding.tvItemDetailsTitle.text = itemDetails.title
                    binding.tvItemDetailsDescription.text = item.description
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}