package com.example.news.data.api

import com.example.news.data.db.ArticleDao
import com.example.news.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
) {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        newsService.getHeadlines(countryCode = countryCode, page = pageNumber)

    suspend fun getSearchNews(query: String, pageNumber: Int) =
        newsService.getEverything(query = query, page = pageNumber)

    fun getFavouriteArticles() = articleDao.getAllArticles()

    suspend fun addToFavourite(article: Article) = articleDao.insert(article)
    suspend fun deleteFromFavourite(article: Article) = articleDao.delete(article)
}