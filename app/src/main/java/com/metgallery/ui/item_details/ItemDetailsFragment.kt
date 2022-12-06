package com.metgallery.ui.item_details

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.metgallery.ui.R
import com.metgallery.ui.databinding.FragmentItemDetailsBinding
import com.metgallery.util.Consts.ARTIST
import com.metgallery.util.Consts.FAVOURITE
import com.metgallery.util.Consts.HEIGHT
import com.metgallery.util.Consts.OBJECT_ID
import com.metgallery.util.Consts.TAG
import com.metgallery.util.Consts.WIDTH
import com.metgallery.util.EventObserver
import com.metgallery.util.setupSnackbar
import com.metgallery.util.showShortSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    private val viewModel: ItemDetailsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentItemDetailsBinding

    private lateinit var bottomNavigation: BottomNavigationView

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
            setOnMenuItemClickListener {
                onMenuItemClicked(it)
            }
        }

        bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation)
        view.setupSnackbar(bottomNavigation, this, viewModel.snackbarText)

        viewDataBinding.rvTags.adapter = TagsAdapter(viewModel)

        presetImageSize()
        setupImagePopupMenu()

        val objectId = arguments?.getInt(OBJECT_ID)
        val favourite = arguments?.getBoolean(FAVOURITE) ?: false
        objectId?.let {
            viewModel.start(objectId, favourite)
        }

        viewModel.selectedTag.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                TAG to it.term
            )
            findNavController().navigate(R.id.action_ItemDetailsFragment_to_CollectionFragment, bundle)
        })

        viewModel.selectedArtist.observe(this.viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(
                ARTIST to it
            )
            findNavController().navigate(R.id.action_ItemDetailsFragment_to_CollectionFragment, bundle)
        })

        viewModel.imageLoading.observe(this.viewLifecycleOwner, EventObserver {
            if (it) {
                viewDataBinding.ivPrimaryImage.setBackgroundColor(resources.getColor(R.color.grey_900, null))
            } else {
                viewDataBinding.ivPrimaryImage.setBackgroundColor(Color.TRANSPARENT)
            }
        })

        viewModel.getLoadingStatus().observe(this.viewLifecycleOwner) {
            viewDataBinding.indeterminateBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    //presetting image size to avoid UI jumping up and down
    private fun presetImageSize() {
        arguments?.let {
            val width = it.getFloat(WIDTH)
            val height = it.getFloat(HEIGHT)

            val displayMetrics = DisplayMetrics()
            (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getMetrics(displayMetrics)

            val newWidth = displayMetrics.widthPixels

            val newHeight = (newWidth * height) / width

            viewDataBinding.ivPrimaryImage.layoutParams.width = newWidth
            viewDataBinding.ivPrimaryImage.layoutParams.height = newHeight.toInt()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupImagePopupMenu() {
        val gestureDetector = GestureDetector(object : SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {

                //showing popup menu near touch place
                viewDataBinding.vPopupMenuAnchor.x = e.x
                viewDataBinding.vPopupMenuAnchor.y = e.y

                val popup = PopupMenu(requireContext(), viewDataBinding.vPopupMenuAnchor)
                popup.menuInflater.inflate(R.menu.menu_item_details, popup.menu)
                popup.setOnMenuItemClickListener {
                    onMenuItemClicked(it)
                }
                popup.show()
            }
        })

        //ZoomageView doesn't work with default context menu functionality easily
        viewDataBinding.ivPrimaryImage.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            false
        }
    }

    private fun onMenuItemClicked(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.action_download -> {
                downloadOriginalImage()
                true
            }
            else -> false
        }

    private fun downloadOriginalImage() {
        viewModel.itemDetails.value?.let {
            downloadImage(it.primaryImage)
        }
    }

    //TODO move to utils? viewmodel?
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
                    view?.showShortSnackbar(resources.getString(R.string.error_downloading_image), bottomNavigation)
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

    //TODO move to view model?
    private fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {
        val imageFile = File(storageDir, imageFileName)

        try {
            val fOut: OutputStream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()

            //notify phone about new file
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), null, null)

            view?.showShortSnackbar(resources.getString(R.string.image_saved), bottomNavigation)
        } catch (e: Exception) {
            view?.showShortSnackbar(resources.getString(R.string.error_saving_image), bottomNavigation)
        }
    }

}