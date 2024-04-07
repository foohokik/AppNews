package com.example.appnews.core

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val PAGE_SIZE = 20

fun<T> Call<T>.onEnqueue(actOnSuccess: (Response<T>) -> Unit, actOnFailure: (t: Throwable?) -> Unit)   {
    this.enqueue(object: Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            actOnSuccess(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            actOnFailure(t)
        }
    })
}