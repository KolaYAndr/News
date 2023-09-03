package com.example.news.ui.fragments.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentDetailsBinding
import com.example.news.models.Article
import com.example.news.utils.ViewControl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val bundleArgs: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailsViewModel>()
    private var liked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ViewControl).hideBottomNavMenu()

        bundleArgs.article.let { article ->
            article.urlToImage.let {
                Glide.with(this)
                    .load(article.urlToImage)
                    .into(binding.detailsImage)
            }
            binding.detailsImage.clipToOutline = false
            binding.articleTitle.text = article.title
            binding.articleDescription.text = article.description

            binding.visitSiteButton.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
                            ?.let {
                                article.url
                            } ?: "https://google.com")).let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "The device doesn't have any browser",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            val likes = viewModel.favouritesLiveData.value
            liked = likes?.contains(article) ?: false
            setLikeButtonImageSource()

            binding.likeButton.setOnClickListener {
                liked = !liked
                setLikeButtonImageSource()
            }

            binding.shareButton.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, article.url)
                    type = "text/link"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
            (activity as ViewControl).showBottomNavMenu()
        }
    }

    private fun setLikeButtonImageSource() {
        when (liked) {
            false -> binding.likeButton.setImageResource(R.drawable.ic_like)
            true -> binding.likeButton.setImageResource(R.drawable.ic_favourite)
        }
    }

    private fun onLeaveAction(article: Article) = when (liked) {
        true -> viewModel.saveToFavourite(article)
        false -> viewModel.deleteFromFavourite(article)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        onLeaveAction(bundleArgs.article)
        _binding = null
    }
}