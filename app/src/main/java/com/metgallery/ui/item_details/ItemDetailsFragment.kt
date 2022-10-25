package com.metgallery.ui.item_details

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentItemDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private val viewModel: ItemDetailsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentItemDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentItemDetailsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            progressBar = this.indeterminateBar
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

        presetImageSize(viewDataBinding.ivPrimaryImage)

        val objectId = arguments?.getInt("objectId")
        val favourite = arguments?.getBoolean("favourite") ?: false
        objectId?.let {
            viewModel.start(objectId, favourite)
        }
    }

    //presetting image size to avoid UI jumping up and down
    private fun presetImageSize(imageView: View) {
        arguments?.let {
            val width = it.getFloat("width")
            val height = it.getFloat("height")

            val displayMetrics = DisplayMetrics()
            (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getMetrics(displayMetrics)

            val newWidth = displayMetrics.widthPixels -
                    resources.getDimensionPixelSize(R.dimen.item_details_image_margin) * 2 -
                    resources.getDimensionPixelSize(R.dimen.item_details_padding) * 2

            val newHeight = (newWidth * height) / width

            viewDataBinding.ivPrimaryImage.layoutParams.width = newWidth
            viewDataBinding.ivPrimaryImage.layoutParams.height = newHeight.toInt()
        }
    }

}