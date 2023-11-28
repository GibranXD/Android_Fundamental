package com.dicoding.restaurantreview.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.databinding.UserItemBinding

class UserAdapter(private val itemsClick: (ItemsItem) -> Unit) : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
    inner class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(
        binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = getItem(position)
                    itemsClick(user)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(itemsItem: ItemsItem) {
            binding.tvUsername.text = itemsItem.login
            binding.userId.text = "# ${itemsItem.id.toString()}"
            binding.userGithubPage.text = itemsItem.htmlUrl
            Glide.with(binding.root.context)
                .load(itemsItem.avatarUrl)
                .into(binding.itemImgView)

            itemView.setOnClickListener {
                itemsClick(itemsItem)
            }
        }

    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}