package com.example.nestwise.data.remote

data class AdviceSlipResponse(
    val slip: Slip
)

data class Slip(
    val id: Int,
    val advice: String
)
