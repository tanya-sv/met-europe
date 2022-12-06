package com.metgallery.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

/**
 * Extension functions
 */

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int, anchorView: View?) {
    Snackbar.make(this, snackbarText, timeLength).run {
        /*addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })*/
        setAnchorView(anchorView)
        show()
    }
}

fun View.showShortSnackbar(snackbarText: String, anchorView: View? = null) {
    showSnackbar(snackbarText, Snackbar.LENGTH_SHORT, anchorView)
}


/**
 * Triggers a snackbar message when the value contained by snackbarEvent is modified.
 */
fun View.setupSnackbar(
    anchorView: View? = null,
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showShortSnackbar(context.getString(it), anchorView)
        }
    })
}