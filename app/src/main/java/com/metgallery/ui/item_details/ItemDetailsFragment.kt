package com.metgallery.ui.item_details

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentItemDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private val viewModel: ItemDetailsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentItemDetailsBinding

    private val storagePermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                downloadOriginalImage()
            }
        }

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
            inflateMenu(R.menu.menu_item_details)
            setOnMenuItemClickListener { menuItem ->

                if (menuItem.itemId == R.id.action_download) {
                    downloadOriginalImage()
                    true
                } else
                    false
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

    private fun downloadOriginalImage() {
        viewModel.itemDetails.value?.let {
            downloadImage(it.primaryImage)
        }
    }

    private fun downloadImage(imageURL: String) {
        if (!verifyPermissions()) {
            return
        }
        val dirPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

        val dir = File(dirPath)
        val fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1)
        Glide.with(this)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    saveImage(bitmap, dir, fileName)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(
                        requireActivity(),
                        "Failed to Download Image! Please try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun verifyPermissions(): Boolean {
        val permission =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            storagePermissionRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return false
        }
        return true
    }

    private fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {
        val imageFile = File(storageDir, imageFileName)

        try {
            val fOut: OutputStream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()

            //notify phone about new file
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.toString()), null, null)

            Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error while saving image!", Toast.LENGTH_LONG).show()
        }
    }

}