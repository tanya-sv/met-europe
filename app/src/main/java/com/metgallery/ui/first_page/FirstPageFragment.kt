package com.metgallery.ui.first_page

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewDataBinding.buttonShow.setOnClickListener {
            val bundle = bundleOf(
                "era" to viewModel.selectedEra,
                "nationality" to viewModel.selectedArtistNationality
            )
            findNavController().navigate(R.id.action_FirstPageFragment_to_CollectionFragment, bundle)
        }
    }

    private fun setupEraSpinner() {
        val adapter = object : ArrayAdapter<EuropeanCollectionEra>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            EuropeanCollectionEra.values()
        ) {

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getDropDownView(position, convertView, parent) as TextView
                textView.text = EuropeanCollectionEra.values()[position].displayValue
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.minHeight = resources.getDimensionPixelSize(R.dimen.default_input_height)
                return textView
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.text = EuropeanCollectionEra.values()[position].displayValue
                return textView
            }
        }

        viewDataBinding.spinnerEra.apply {
            this.adapter = adapter

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
        val adapter = object : ArrayAdapter<ArtistNationality>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArtistNationality.values()
        ) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getDropDownView(position, convertView, parent) as TextView
                textView.text = ArtistNationality.values()[position].displayValue
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.minHeight = resources.getDimensionPixelSize(R.dimen.default_input_height)
                return textView
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.text = ArtistNationality.values()[position].displayValue
                return textView
            }
        }

        viewDataBinding.spinnerArtisNationality.apply {
            this.adapter = adapter

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