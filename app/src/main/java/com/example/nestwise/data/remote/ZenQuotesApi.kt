package com.example.nestwise.data.remote

import retrofit2.http.GET

interface ZenQuotesApi {

    @GET("today")
    suspend fun getTodayQuote(): List<ZenQuoteItem>
}
