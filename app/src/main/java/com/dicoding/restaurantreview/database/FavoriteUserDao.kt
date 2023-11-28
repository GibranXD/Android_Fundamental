package com.dicoding.restaurantreview.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE login = :login")
    fun checkFav(login: String) : LiveData<FavoriteUser>



}