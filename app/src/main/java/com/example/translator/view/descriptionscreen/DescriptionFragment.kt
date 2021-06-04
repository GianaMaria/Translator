package com.example.translator.view.descriptionscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.translator.R
import com.example.translator.view.main.MainActivity
import com.example.utils.network.OnlineLiveData
import com.example.utils.ui.AlertDialogFragment
import com.example.utils.ui.viewById
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_description.*

class DescriptionFragment : Fragment() {

    private val descriptionHeader by viewById<TextView>(R.id.description_header)
    private val descriptionTextView by viewById<TextView>(R.id.description_textview)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setActionbarHomeButtonAsUp()
        description_screen_swipe_refresh_layout.setOnRefreshListener { startLoadingOrShowError() }
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData() {
        val word = arguments?.getString(WORD_EXTRA)
        val translate = arguments?.getString(DESCRIPTION_EXTRA)
        val imageUrl = arguments?.getString(URL_EXTRA)

        descriptionHeader.text = word
        descriptionTextView.text = translate
        if (imageUrl.isNullOrBlank()) {
            stopRefreshAnimationIfNeeded()
        } else {
            usePicassoToLoadPhoto(description_imageview, imageUrl)
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        val activity = activity as MainActivity
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun startLoadingOrShowError() {
        this.context?.let {
            OnlineLiveData(it).observe(
                this@DescriptionFragment,
                Observer<Boolean> {
                    if (it) {
                        setData()
                    } else {
                        AlertDialogFragment.newInstance(
                            getString(R.string.dialog_title_device_is_offline),
                            getString(R.string.dialog_message_device_is_offline)
                        ).show(
                            fragmentManager!!,
                            DIALOG_FRAGMENT_TAG
                        )
                        stopRefreshAnimationIfNeeded()
                    }
                }
            )
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (description_screen_swipe_refresh_layout.isRefreshing) {
            description_screen_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun usePicassoToLoadPhoto(imageView: ImageView, imageLink: String) {
        Picasso.with(activity?.applicationContext).load("https:$imageLink")
            .placeholder(R.drawable.ic_no_photo_vector).fit().centerCrop()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    stopRefreshAnimationIfNeeded()
                }

                override fun onError() {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_load_error_vector)
                }
            })
    }

    companion object {

        private const val DIALOG_FRAGMENT_TAG = "8c7dff51-9769-4f6d-bbee-a3896085e76e"
        private const val WORD_EXTRA = "f76a288a-5dcc-43f1-ba89-7fe1d53f63b0"
        private const val DESCRIPTION_EXTRA = "0eeb92aa-520b-4fd1-bb4b-027fbf963d9a"
        private const val URL_EXTRA = "6e4b154d-e01f-4953-a404-639fb3bf7281"

        fun newInstance(word: String?, description: String, url: String?): DescriptionFragment =
            DescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(WORD_EXTRA, word)
                    putString(DESCRIPTION_EXTRA, description)
                    putString(URL_EXTRA, url)
                }
            }
    }
}