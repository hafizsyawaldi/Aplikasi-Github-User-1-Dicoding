package com.dicoding.githubuserapi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapi.R.id
import com.dicoding.githubuserapi.R.string
import com.dicoding.githubuserapi.adapter.UserAdapter
import com.dicoding.githubuserapi.databinding.ActivityMainBinding
import com.dicoding.githubuserapi.response.ItemsItem
import com.dicoding.githubuserapi.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = UserAdapter()
    private lateinit var Mainbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mainbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Mainbinding.root)

        showViewModel()
        showRecyclerView()
        viewModel.getIsLoading.observe(this, this::showLoading)
        viewModel.searchUser("q")
        Mainbinding.tvNotFound.visibility = View.GONE
    }

    private fun showViewModel() {
        viewModel.getSearchList.observe(this) { searchList ->
            if (searchList.size != 0) {
                Mainbinding.tvNotFound.visibility = View.GONE
                Mainbinding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchList)
            } else {
                Mainbinding.tvNotFound.visibility = View.VISIBLE
                Mainbinding.rvUser.visibility = View.GONE
                Toast.makeText(this, "User Not Found!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        Mainbinding.rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        Mainbinding.rvUser.setHasFixedSize(true)
        Mainbinding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: ItemsItem) {
        val i = Intent(this, UserDetailActivity::class.java)
        i.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(id.search).actionView as SearchView
        val close = menu.findItem(id.search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        close.icon?.setVisible(false, false)

        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            Mainbinding.progressBar.visibility = View.VISIBLE
        } else {
            Mainbinding.progressBar.visibility = View.GONE
        }
    }
}