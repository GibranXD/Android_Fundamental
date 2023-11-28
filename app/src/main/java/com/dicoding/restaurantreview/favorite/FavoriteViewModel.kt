package com.dicoding.restaurantreview.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.restaurantreview.database.FavoriteUser
import com.dicoding.restaurantreview.repository.FavoriteUserRepository

class FavoriteViewModel (application: Application): ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavorites() : LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavorites()

}