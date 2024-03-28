package com.dicoding.githubuserapi

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapi.adapter.SectionsPageAdapter
import com.dicoding.githubuserapi.databinding.ActivityUserDetailBinding
import com.dicoding.githubuserapi.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var Detailbinding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Detailbinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(Detailbinding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPageAdapter(this)
        val viewPager: ViewPager2 = Detailbinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = Detailbinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel()
        viewModel.getIsLoading.observe(this, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.detailUser(username)
        viewModel.getUserDetail.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(Detailbinding.imgAvatar)

            Detailbinding.tvName.text = detailUser.name
            Detailbinding.tvUsername.text = detailUser.login
            Detailbinding.tvCompany.text = detailUser.company
            Detailbinding.tvLocation.text = detailUser.location
            Detailbinding.tvRepositoryValue.text = detailUser.publicRepos
            Detailbinding.tvFollowersValue.text = detailUser.followers
            Detailbinding.tvFollowingValue.text = detailUser.following
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            Detailbinding.progressBar.visibility = View.VISIBLE
        } else {
            Detailbinding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}