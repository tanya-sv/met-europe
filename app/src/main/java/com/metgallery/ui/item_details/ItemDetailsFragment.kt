package com.metgallery.ui.item_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metgallery.data.api.model.MetCollectionItem
import com.metgallery.ui.databinding.FragmentItemDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private val viewModel: ItemDetailsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentItemDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = FragmentItemDetailsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        val item = arguments?.getParcelable<MetCollectionItem>("itemDetails")
        item?.let {
            viewModel.start(it)
        }
    }
}