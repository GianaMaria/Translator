package com.example.translator.view.main

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.example.translator.R
import com.example.translator.model.data.AppState
import com.example.translator.model.data.DataModel
import com.example.translator.utils.convertMeaningsToString
import com.example.translator.utils.network.isOnline
import com.example.translator.view.base.BaseFragment
import com.example.translator.view.descriptionscreen.DescriptionFragment
import com.example.translator.view.history.HistoryFragment
import com.example.translator.view.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"

class MainFragment : BaseFragment<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val fabClickListener: View.OnClickListener =
        View.OnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
            parentFragmentManager.let { it1 ->
                searchDialogFragment.show(
                    it1,
                    BOTTOM_SHEET_FRAGMENT_DIALOG_TAG
                )
            }
        }
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.container, DescriptionFragment.newInstance(
                        data.text,
                        convertMeaningsToString(data.meanings!!),
                        data.meanings[0].imageUrl
                    )
                ).addToBackStack(null)
                    .commit()
            }
        }
    private val onSearchClickListener: SearchDialogFragment.OnSearchClickListener =
        object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                isNetworkAvailable = activity?.let { isOnline(it.applicationContext) } == true
                if (isNetworkAvailable) {
                    model.getData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniViewModel()
        initViews()
        main_activity_recyclerview.adapter = adapter
        setActionbarHomeButtonAsUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewModel()
        initViews()
        main_activity_recyclerview.adapter = adapter
        setActionbarHomeButtonAsUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoryFragment()).addToBackStack(null).commit()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniViewModel() {
        if (main_activity_recyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })

    }

    private fun initViews() {
        search_fab.setOnClickListener(fabClickListener)
        main_activity_recyclerview.adapter = adapter
    }

    private fun setActionbarHomeButtonAsUp() {
        val activity = activity as MainActivity
        activity.supportActionBar?.setHomeButtonEnabled(false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
