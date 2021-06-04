package com.example.translator.view.main

import android.app.Activity.RESULT_OK
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.core.BaseFragment
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.translator.R
import com.example.translator.di.injectDependencies
import com.example.translator.utils.convertMeaningsToString
import com.example.translator.view.descriptionscreen.DescriptionFragment
import com.example.translator.view.main.adapter.MainAdapter
import com.example.utils.ui.viewById
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.android.scope.currentScope

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
private const val HISTORY_FRAGMENT_PATH = "com.example.historyscreen.view.HistoryFragment"
private const val HISTORY_FRAGMENT_FEATURE_NAME = "history"
private const val MAIN_FRAGMENT_SETTINGS_REQUEST_CODE = 42

class MainFragment : BaseFragment<AppState, MainInteractor>() {

    private lateinit var splitInstallManager: SplitInstallManager

    override lateinit var model: MainViewModel
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val recyclerView by viewById<RecyclerView>(R.id.main_activity_recyclerview)
    private val fab by viewById<FloatingActionButton>(R.id.search_fab)

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
        val viewModel: MainViewModel by currentScope.inject()
        model = viewModel
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    model.getData(searchWord, true)
                        .observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })

                }
            })
            searchDialogFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history_menu, menu)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showHistoryFragment()
                setActionbarHomeButtonAsUp(true)
                return true
            }
            R.id.menu_settings -> {
                startActivityForResult(
                    Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),
                    MAIN_FRAGMENT_SETTINGS_REQUEST_CODE
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHistoryFragment() {
        val fragment = Class.forName(HISTORY_FRAGMENT_PATH)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == MAIN_FRAGMENT_SETTINGS_REQUEST_CODE){
            Toast.makeText(this.context, "result_ok", Toast.LENGTH_SHORT).show()
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
