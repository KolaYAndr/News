package com.example.news.ui.fragments.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.api.NewsRepository
import com.example.news.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {
    init {
        getSavedArticles()
    }
    fun getSavedArticles(): List<Article> {
        val articles = mutableListOf<Article>()
        viewModelScope.launch(Dispatchers.IO) {
            articles.addAll(repository.getFavouriteArticles())
        }
        return articles.toList()
    }
    fun saveToFavourite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repository.addToFavourite(article)
    }

    fun deleteFromFavourite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        val res = repository.getFavouriteArticles()

        println("before delete DB size: ${res.size}")
        repository.deleteFromFavourite(article)
        val res1 = repository.getFavouriteArticles()
        println("after delete DB size: ${res1.size}")
    }
}