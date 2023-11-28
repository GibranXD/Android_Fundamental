package com.dicoding.restaurantreview.ui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.restaurantreview.setting.SettingPreferences
import com.dicoding.restaurantreview.data.response.GithubResponse
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList


     fun findUser(username: String):Boolean{
        _isLoading.value = true
        val client = ApiConfig.getApiService().findUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    _userList.value = items ?: ArrayList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
         return false

    }

    fun getThemeSettings(): LiveData<Boolean>{
        return pref.getThemeSetting().asLiveData()
    }


}