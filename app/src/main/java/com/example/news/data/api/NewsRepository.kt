package com.example.news.data.api

import com.example.news.utils.Constants.Companion.API_KEY
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsService: NewsService) {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        newsService.getHeadlines(countryCode = countryCode, page = pageNumber)

    suspend fun getSearchNews(query: String, pageNumber: Int) =
        newsService.getEverything(query = query, page = pageNumber)

    suspend fun getHeadlines(pageNumber: Int) =
        newsService.getHeadlines(apiKey = API_KEY, page = pageNumber)
}