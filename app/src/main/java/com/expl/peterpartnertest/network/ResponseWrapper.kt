package com.expl.peterpartnertest.network

sealed class ResponseWrapper<out T: Any> {
    data class Success<out T : Any>(val data: T) : ResponseWrapper<T>()
    data class Error(val exception: String) : ResponseWrapper<Nothing>()
}