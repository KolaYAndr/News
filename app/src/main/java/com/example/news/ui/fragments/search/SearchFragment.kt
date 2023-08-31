package com.example.news.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.ui.adapters.NewsAdapter
import com.example.news.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchNewsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        searchNewsAdapter.setOnItemClickListener {
            val bundle = bundleOf("article" to it)
            view.findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment,
                bundle
            )

            activity.let {activity ->
                (activity as MainActivity).hideBottomNavMenu()
            }
        }

        var job: Job? = null

        binding.searchEditText.addTextChangedListener { text ->
            job?.cancel()
            job = MainScope().launch {
                text?.let {
                    if (it.toString().isNotBlank()) {
                        viewModel.getSearchNews(query = it.toString())
                    }
                }
            }
        }

        viewModel.searchNewsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    response.data?.let {
                        searchNewsAdapter.differ.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    response.data?.let {
                        Log.e("checkData", "SearchFragment: error: $it")
                    }
                }

                is Resource.Loading -> {
                    binding.searchProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        searchNewsAdapter = NewsAdapter()
        binding.searchNewsAdapter.apply {
            adapter = searchNewsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}