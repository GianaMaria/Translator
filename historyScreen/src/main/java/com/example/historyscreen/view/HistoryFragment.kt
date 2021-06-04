package com.example.historyscreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.core.BaseFragment
import com.example.historyscreen.R
import com.example.historyscreen.injectDependencies
import com.example.model.data.AppState
import com.example.model.data.DataModel
import com.example.utils.ui.viewById
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment<AppState, HistoryInteractor>() {

    override lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }
    private val historyFragmentRecyclerView by viewById<RecyclerView>(R.id.history_fragment_recyclerview)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        model.getData("", false).observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
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

    private fun initViews() {
        setHasOptionsMenu(true)
        injectDependencies()
        history_fragment_recyclerview.adapter = adapter
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }
}