package com.metgallery.ui.first_page

import android.os.Bundle

import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.metgallery.data.model.ArtistNationality
import com.metgallery.data.model.EuropeanCollectionEra
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentFirstPageBinding
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

        setupEraSpinner()
        setupArtistNationalitySpinner()

        viewDataBinding.toolbar.apply {
            inflateMenu(R.menu.menu_first_page)
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_favourites) {
                    val bundle = bundleOf(
                        "favourites" to true
                    )
                    findNavController().navigate(R.id.action_FirstPageFragment_to_CollectionFragment, bundle)
                }
                true
            }
        }

        viewDataBinding.buttonExplore.setOnClickListener {
            val bundle = bundleOf(
                "era" to viewModel.selectedEra,
                "nationality" to viewModel.selectedArtistNationality,
                "excludeMiniatures" to viewModel.excludeMiniatures
            )
            findNavController().navigate(R.id.action_FirstPageFragment_to_CollectionFragment, bundle)
        }
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

    private fun setupEraSpinner() {
        viewDataBinding.spinnerEra.apply {
            this.adapter = createSpinnerAdapter(EuropeanCollectionEra.values())

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    viewModel.selectedEra = EuropeanCollectionEra.values()[position]
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun setupArtistNationalitySpinner() {
        viewDataBinding.spinnerArtisNationality.apply {
            this.adapter = createSpinnerAdapter(ArtistNationality.values())

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    viewModel.selectedArtistNationality = ArtistNationality.values()[position]
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

}