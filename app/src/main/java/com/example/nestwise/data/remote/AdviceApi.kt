package com.example.nestwise.data.remote

import retrofit2.http.GET

interface AdviceApi {

    @GET("advice")
    suspend fun getRandomAdvice(): AdviceSlipResponse
}
