package com.dicoding.restaurantreview.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.databinding.FragmentFollowersBinding


class FollowersFragment : Fragment() {

    private lateinit var binding : FragmentFollowersBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        viewModel.userData.observe(viewLifecycleOwner){userData ->
            userData.login?.let {
                val isError = viewModel.getFollowers(it)
                if(isError) Toast.makeText(requireContext(), "Cannot retrieve followers data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userFollowers.observe(viewLifecycleOwner) { followerList ->
            setFollowersData(followerList)
        }

        viewModel.isFragmentLoading.observe(viewLifecycleOwner){
            setProgressBar(it)
        }
    }

    private fun setFollowersData(followerList: List<ItemsItem>) {
        binding.listFollower.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UserAdapter{user ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("username", user.login )
            startActivity(intent)
        }
        adapter.submitList(followerList)
        binding.listFollower.adapter = adapter
    }

    private fun setProgressBar(isLoading:Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

}