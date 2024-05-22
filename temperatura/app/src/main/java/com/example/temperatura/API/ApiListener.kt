package com.example.temperatura.API

interface ApiListener {
    fun onApiResponse(result: String?)
    fun onApiError(e: Exception?)
}
