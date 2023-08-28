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
    fun getSavedArticles():List<Article> {
        var articles: List<Article> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            articles = repository.getFavouriteArticles()
        }
        return articles
    }

    fun saveToFavourite(article: Article) = viewModelScope.launch(Dispatchers.IO){
        repository.addToFavourite(article)
    }

    fun deleteFromFavourite(article: Article) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteFromFavourite(article)
    }
}