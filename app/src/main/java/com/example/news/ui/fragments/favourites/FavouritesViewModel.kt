package com.example.news.ui.fragments.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.api.NewsRepository
import com.example.news.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: NewsRepository) :
    ViewModel() {
    val favouritesNewsLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    init {
        getFavourites()
    }

    private fun getFavourites() = viewModelScope.launch(Dispatchers.IO) {
        favouritesNewsLiveData.postValue(repository.getFavouriteArticles())
    }

}