package com.tsvetova.metgallery.ui.first_page

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsvetova.metgallery.data.model.ArtistNationality
import com.tsvetova.metgallery.data.model.EuropeanCollectionEra
import com.tsvetova.metgallery.ui.R
import com.tsvetova.metgallery.ui.databinding.FragmentFirstPageBinding
import com.tsvetova.metgallery.util.Consts.ERA
import com.tsvetova.metgallery.util.Consts.NATIONALITY
import com.tsvetova.metgallery.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstPageFragment : Fragment() {

    private val viewModel: FirstPageViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentFirstPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentFirstPageBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.spinnerEra.adapter = createSpinnerAdapter(EuropeanCollectionEra.values())
        viewDataBinding.spinnerArtisNationality.adapter = createSpinnerAdapter(ArtistNationality.values())

        viewModel.selectedFilters.observe(this.viewLifecycleOwner, EventObserver { selectedFilters ->
            val bundle = bundleOf(
                ERA to selectedFilters.first,
                NATIONALITY to selectedFilters.second,
            )
            findNavController().navigate(R.id.action_FirstPageFragment_to_CollectionFragment, bundle)
        })
    }

    private fun <T> createSpinnerAdapter(values: Array<T>): ArrayAdapter<T> {
        val adapter = object : ArrayAdapter<T>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            values
        ) {

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getDropDownView(position, convertView, parent) as TextView
                textView.text = values[position].toString()
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.minHeight = resources.getDimensionPixelSize(R.dimen.spinner_dropdown_height)
                return textView
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.text = values[position].toString()
                return textView
            }
        }

        return adapter
    }
}