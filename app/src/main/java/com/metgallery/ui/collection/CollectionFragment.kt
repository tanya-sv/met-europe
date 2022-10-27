package com.metgallery.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
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

        viewDataBinding.toolbar.apply {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewDataBinding.rvCollection.adapter = CollectionAdapter(viewModel)

        val favourites = arguments?.getBoolean("favourites") ?: false
        if (!favourites) {
            viewDataBinding.toolbar.apply {
                inflateMenu(R.menu.menu_collection_page)
                setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.action_favourites) {
                        val bundle = bundleOf(
                            "favourites" to true
                        )
                        findNavController().navigate(
                            R.id.action_CollectionFragment_to_CollectionFragment,
                            bundle
                        )
                    }
                    true
                }
            }
        }

        val era = arguments?.getSerializable("era") as EuropeanCollectionEra? ?: EuropeanCollectionEra.None
        val artistNationality =
            arguments?.getSerializable("nationality") as ArtistNationality? ?: ArtistNationality.None
        val excludeMiniatures = arguments?.getBoolean("excludeMiniatures") ?: false

        viewModel.items.observe(this.viewLifecycleOwner) {
            val title =
                if (favourites) resources.getString(R.string.favourites)
                else "${era.displayNameOrEmpty()}  ${artistNationality.displayNameOrEmpty()}"

            viewDataBinding.toolbar.title = "$title (${it.size})"
        }

        viewModel.loadCollection(favourites, artistNationality, era, excludeMiniatures)

        viewModel.selectedItem.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                "objectId" to it.objectId,
                "width" to it.width,
                "height" to it.height,
                "favourite" to it.favourite
            )
            findNavController().navigate(R.id.action_CollectionFragment_to_ItemDetailsFragment, bundle)
        })
    }
}