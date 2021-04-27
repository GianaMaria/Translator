package com.example.translator.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.translator.R
import com.example.translator.model.data.AppState
import com.example.translator.utils.network.isOnline
import com.example.translator.utils.ui.AlertDialogFragment
import com.example.translator.viewModel.BaseViewModel
import com.example.translator.viewModel.Interactor

abstract class BaseFragment<T : AppState, I : Interactor<T>> : Fragment() {

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false

    val contextFragment = activity?.applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkAvailable = activity?.let { isOnline(it.applicationContext) } == true
    }

    override fun onResume() {
        super.onResume()
        isNetworkAvailable = activity?.let { isOnline(it.applicationContext) } == true
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        fragmentManager?.let {
            AlertDialogFragment.newInstance(title, message).show(it, DIALOG_FRAGMENT_TAG)
        }
    }

    private fun isDialogNull(): Boolean {
        return fragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    abstract fun renderData(dataModel: T)

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "74a54328-5d62-46bf-ab6b-cbf5d8c79522"
    }
}