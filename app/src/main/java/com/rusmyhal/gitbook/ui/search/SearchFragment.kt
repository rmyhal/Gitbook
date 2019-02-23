package com.rusmyhal.gitbook.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rusmyhal.gitbook.R
import com.rusmyhal.gitbook.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.viewModel


class SearchFragment : BaseFragment() {

    override val layoutRes = R.layout.fragment_search

    private val viewModel: SearchViewModel by viewModel()

    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter { username -> viewModel.onUserClick(username) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        subscribe()
    }

    private fun subscribe() {
        viewModel.run {
            users.observe(viewLifecycleOwner, Observer { users ->
                users?.let {
                    usersAdapter.updateData(it)
                }
            })

            error.observe(viewLifecycleOwner, Observer { error ->
                Toast.makeText(
                    context,
                    error ?: getString(R.string.error_default),
                    Toast.LENGTH_SHORT
                ).show()
            })

            loading.observe(viewLifecycleOwner, Observer { isShow ->
                if (isShow) progress.show() else progress.hide()
            })
        }
    }

    companion object {

        const val TAG = "SearchFragment"
    }
}