package com.example.translator.view.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.core.BaseFragment
import com.example.historyscreen.injectDependencies
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.translator.R
import com.example.translator.utils.convertMeaningsToString
import com.example.translator.view.descriptionscreen.DescriptionFragment
import com.example.translator.view.main.adapter.MainAdapter
import com.example.utils.network.isOnline
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
private const val HISTORY_FRAGMENT_PATH = "com.example.historyscreen.view.HistoryFragment"
private const val HISTORY_FRAGMENT_FEATURE_NAME = "history"

class MainFragment : BaseFragment<AppState, MainInteractor>() {

    private lateinit var splitInstallManager: SplitInstallManager

    override val model by viewModel<MainViewModel>()
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                fragmentManager?.beginTransaction()?.replace(
                    R.id.container, DescriptionFragment.newInstance(
                        data.text,
                        convertMeaningsToString(data.meanings!!),
                        data.meanings!![0].imageUrl
                    )
                )?.addToBackStack(null)
                    ?.commit()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setActionbarHomeButtonAsUp(false)
        injectDependencies()
        main_activity_recyclerview.adapter = adapter

        search_fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    isNetworkAvailable = activity?.let { isOnline(it.applicationContext) } == true
                    if (isNetworkAvailable) {
                        model.getData(searchWord, isNetworkAvailable)
                            .observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
                    } else {
                        showNoInternetConnectionDialog()
                    }
                }
            })
            searchDialogFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showHistoryFragment()
                setActionbarHomeButtonAsUp(true)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHistoryFragment() {
        val fragment = Class.forName("com.example.historyscreen.view.HistoryFragment")
            .newInstance() as Fragment
        splitInstallManager = SplitInstallManagerFactory.create(this.context)
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(HISTORY_FRAGMENT_FEATURE_NAME)
            .build()

        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this.context,
                    "Couldn't download feature: " + it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun setActionbarHomeButtonAsUp(showButton: Boolean) {
        val activity = activity as MainActivity
        activity.supportActionBar?.setHomeButtonEnabled(false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
