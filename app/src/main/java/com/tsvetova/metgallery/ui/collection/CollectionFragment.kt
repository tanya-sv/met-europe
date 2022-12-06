package com.tsvetova.metgallery.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsvetova.metgallery.ui.R
import com.tsvetova.metgallery.ui.databinding.FragmentCollectionBinding
import com.tsvetova.metgallery.util.Consts.FAVOURITE
import com.tsvetova.metgallery.util.Consts.HEIGHT
import com.tsvetova.metgallery.util.Consts.OBJECT_ID
import com.tsvetova.metgallery.util.Consts.WIDTH
import com.tsvetova.metgallery.util.EventObserver
import com.tsvetova.metgallery.util.setupSnackbar
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

        arguments?.let {
            viewModel.readFromBundle(it)
        }

        if (!viewModel.isFavouritesOnly()) {
            viewDataBinding.toolbar.apply {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

        view.setupSnackbar(
            requireActivity().findViewById(R.id.bottom_navigation),
            this,
            viewModel.snackbarText
        )

        viewDataBinding.rvCollection.adapter = CollectionAdapter(viewModel)

        viewModel.items.observe(this.viewLifecycleOwner) {
            val title =
                if (viewModel.isFavouritesOnly()) resources.getString(R.string.favourites_fragment_label)
                else if (!viewModel.getTag().isNullOrBlank()) "#${viewModel.getTag()}"
                else if (!viewModel.getArtist().isNullOrBlank()) "${viewModel.getArtist()}"
                else if (viewModel.getEra().isNone() && viewModel.getArtistNationality().isNone()) "All"
                else "${viewModel.getEra().displayNameOrEmpty()}  ${
                    viewModel.getArtistNationality().displayNameOrEmpty()
                }"

            viewDataBinding.toolbar.title = "$title (${it.size})"
        }

        viewModel.loadCollection()

        viewModel.selectedItem.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                OBJECT_ID to it.objectId,
                WIDTH to it.width,
                HEIGHT to it.height,
                FAVOURITE to it.favourite
            )
            findNavController().navigate(R.id.action_CollectionFragment_to_ItemDetailsFragment, bundle)
        })
    }
}