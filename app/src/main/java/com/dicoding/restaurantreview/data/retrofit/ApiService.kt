package com.dicoding.restaurantreview.data.retrofit

import com.dicoding.restaurantreview.data.response.DetailResponse
import com.dicoding.restaurantreview.data.response.GithubResponse
import com.dicoding.restaurantreview.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/search/users")
    fun findUser(@Query("q") username: String) : Call<GithubResponse>

    @GET("/users/{username}")
    fun getUserData(@Path("username") username: String) : Call<DetailResponse>

    @GET("/users/{username}/following")
    fun getFollowing(@Path("username") username: String) : Call<List<ItemsItem>>

    @GET("/users/{username}/followers")
    fun getFollowers(@Path("username") username: String) : Call<List<ItemsItem>>
}