package com.dicoding.restaurantreview.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.restaurantreview.database.FavoriteUser
import com.dicoding.restaurantreview.database.FavoriteUserDao
import com.dicoding.restaurantreview.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository (application: Application){

    private val mFavoritesDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init{
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteUser>> = mFavoritesDao.getAllFavorites()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoritesDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser){
        executorService.execute { mFavoritesDao.delete(favoriteUser) }
    }

    fun check(login: String): LiveData<FavoriteUser> {
        return mFavoritesDao.checkFav(login)
    }
}