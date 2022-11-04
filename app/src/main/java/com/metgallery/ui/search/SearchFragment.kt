package com.metgallery.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentSearchBinding
import com.metgallery.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.rvSearchResults.adapter = SearchAdapter(viewModel)

        viewDataBinding.etSearchTerm.addTextChangedListener(object : TextWatcher {
            private var searchTerm = ""

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = text.toString().trim()
                if (searchText == searchTerm)
                    return

                searchTerm = searchText

                //debounce
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    if (searchText != searchTerm)
                        return@launch

                    if (searchTerm.isNotBlank()) {
                        viewModel.search(searchTerm)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        viewModel.selectedSearchResult.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                it.argumentName to it.term
            )
            findNavController().navigate(R.id.action_SearchFragment_to_CollectionFragment, bundle)
        })
    }

}