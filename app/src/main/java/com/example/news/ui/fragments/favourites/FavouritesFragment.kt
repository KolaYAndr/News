package com.example.news.ui.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentFavouritesBinding
import com.example.news.ui.adapters.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavouritesViewModel>()
    private lateinit var favouriteNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.favouritesNewsLiveData.value?.isNotEmpty() == true) {
            binding.noFavourites.visibility = View.GONE
            initAdapter()
            binding.favouritesAdded.visibility = View.VISIBLE

            favouriteNewsAdapter.setOnItemClickListener {
                val bundle = bundleOf("article" to it)
                view.findNavController().navigate(
                    R.id.action_favouritesFragment_to_detailsFragment,
                    bundle
                )
            }
        }
    }

    private fun initAdapter() {
        favouriteNewsAdapter = NewsAdapter()
        binding.favouriteNewsAdapter.apply {
            adapter = favouriteNewsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}