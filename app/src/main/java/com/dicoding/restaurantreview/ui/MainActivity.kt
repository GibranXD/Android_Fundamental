package com.dicoding.restaurantreview.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.setting.SettingPreferences
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.setting.dataStore
import com.dicoding.restaurantreview.databinding.ActivityMainBinding
import com.dicoding.restaurantreview.favorite.FavoriteActivity
import com.dicoding.restaurantreview.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(pref, application)).get(MainViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = "Github App"

        mainViewModel.userList.observe(this) { userList ->
            setUserListData(userList)
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    if(!searchView.text.isNullOrEmpty()){
                        searchBar.text = searchView.text
                        val isError = mainViewModel.findUser(searchView.text.toString())
                        searchView.hide()
                        if(isError) Toast.makeText(this@MainActivity, "Cannot retrieve users data", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
        }

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
            R.id.action_favorite -> {
                val moveIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserListData(userList: List<ItemsItem>) {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter{ user ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", user.login)
            startActivity(intent)
        }
        adapter.submitList(userList)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}