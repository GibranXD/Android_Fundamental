package com.dicoding.restaurantreview.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.response.DetailResponse
import com.dicoding.restaurantreview.database.FavoriteUser
import com.dicoding.restaurantreview.databinding.ActivityUserDetailBinding
import com.dicoding.restaurantreview.setting.SettingPreferences
import com.dicoding.restaurantreview.setting.dataStore
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    private lateinit var detailViewModel: DetailViewModel

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_following, R.string.tab_followers)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")

        val userViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        val pref = SettingPreferences.getInstance(application.dataStore)
        detailViewModel = obtainViewModel(this, pref)

        if (username != null) {
            val isError = userViewModel.getUserData(username)
            if(isError) Toast.makeText(this@DetailActivity, "Cannot retrieve user detail data", Toast.LENGTH_SHORT).show()
        }

        userViewModel.userData.observe(this){userData ->
            setUserData(userData)
        }

        userViewModel.isLoading.observe(this){
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionPageAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs : TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager){ tab , position ->
            tab.text  = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        var check = false

        detailViewModel.checkFav(username.toString()).observe(this){ userFav ->
            check = if (userFav != null ) {
                binding.fabFav.setImageDrawable(ContextCompat.getDrawable(binding.fabFav.context, R.drawable.baseline_favorite_24))
                true
            } else {
                binding.fabFav.setImageDrawable(ContextCompat.getDrawable(binding.fabFav.context, R.drawable.baseline_favorite_border_24))
                false
            }
        }

        binding.fabFav.setOnClickListener {
            if (check){
                detailViewModel.userData.observe(this){ userData ->
                    removeFavorite(userData)
                }
            } else {
                detailViewModel.userData.observe(this){ userData ->
                    addToFavorite(userData)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUserData(userData: DetailResponse) {
        Glide.with(this)
            .load(userData.avatarUrl)
            .into(binding.avatar)

        with(binding) {
            nama.text = userData.name
            bio.text = userData.bio
            username.text = "#" + userData.login
            followers.text = "Followers : " + userData.followers
            following.text = "Following : " + userData.following
        }
    }

    private fun addToFavorite(userData: DetailResponse){
        val favoriteUser = userData.login?.let {
            FavoriteUser(
                userData.id,
                userData.avatarUrl,
                userData.login
            )
        }
        detailViewModel.insert(favoriteUser as FavoriteUser)
    }

    private fun removeFavorite(userData: DetailResponse){
        val favoriteUser = userData.login?.let {
            FavoriteUser(
                userData.id,
                userData.avatarUrl,
                userData.login
            )
        }
        detailViewModel.delete(favoriteUser as FavoriteUser)
    }

    private fun obtainViewModel(activity: AppCompatActivity, pref: SettingPreferences): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }


}