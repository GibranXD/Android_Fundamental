package com.dicoding.restaurantreview.ui

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.restaurantreview.data.response.DetailResponse
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import com.dicoding.restaurantreview.database.FavoriteUser
import com.dicoding.restaurantreview.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application){

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser){
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser){
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun checkFav(login: String): LiveData<FavoriteUser> {
        return mFavoriteUserRepository.check(login)
    }


    private val _userData = MutableLiveData<DetailResponse>()
    val userData: LiveData<DetailResponse> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFragmentLoading = MutableLiveData<Boolean>()
    val isFragmentLoading: LiveData<Boolean> = _isFragmentLoading

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private val _userFollowers = MutableLiveData<List<ItemsItem>>()
    val userFollowers: LiveData<List<ItemsItem>> = _userFollowers


    fun getUserData(username: String): Boolean{
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserData(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _userData.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return false

    }

    fun getFollowing(username: String): Boolean{
        _isFragmentLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isFragmentLoading.value = false
                    _userFollowing.value = response.body()
                } else {
                    _isFragmentLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
        return false
    }

    fun getFollowers(username: String): Boolean{
        _isFragmentLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isFragmentLoading.value = false
                    _userFollowers.value = response.body()
                } else {
                    _isFragmentLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false

            }
        })
        return false

    }



}