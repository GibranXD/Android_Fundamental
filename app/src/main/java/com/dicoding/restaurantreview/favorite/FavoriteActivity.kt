package com.dicoding.restaurantreview.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.database.FavoriteUser
import com.dicoding.restaurantreview.databinding.ActivityFavoriteBinding
import com.dicoding.restaurantreview.setting.SettingActivity
import com.dicoding.restaurantreview.setting.SettingPreferences
import com.dicoding.restaurantreview.setting.dataStore
import com.dicoding.restaurantreview.ui.DetailActivity
import com.dicoding.restaurantreview.ui.UserAdapter
import com.dicoding.restaurantreview.ui.ViewModelFactory

class FavoriteActivity: AppCompatActivity() {
    private lateinit var favoriteViewModel : FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

        val pref = SettingPreferences.getInstance(application.dataStore)
        favoriteViewModel = obtainViewModel(this, pref)

        favoriteViewModel.getAllFavorites().observe(this){ favoriteUser ->
            val usersItemList = favoriteUser.map { mapToFavoriteUsersItem(it)}
            setUserListData(usersItemList)

        }
    }

    private fun setUserListData(userList: List<ItemsItem>){
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter{user ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username",user.login)
            startActivity(intent)
        }
        adapter.submitList(userList)
        binding.rvFavorite.adapter = adapter
    }

    private fun mapToFavoriteUsersItem(favoriteUser: FavoriteUser) : ItemsItem{
        return ItemsItem(
            id = favoriteUser.id,
            avatarUrl = favoriteUser.avatarUrl,
            login = favoriteUser.login
        )
    }

    private fun obtainViewModel(activity: AppCompatActivity, pref: SettingPreferences) : FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_setting -> {
                val moveIntent = Intent(this, SettingActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}