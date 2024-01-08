package com.example.appnews.core

import retrofit2.Response

class HttpResultToDataWrapperConverter() {

    fun <T> convert(result: Response<T>): DataWrapper<T> {
        return if (result.isSuccessful) {
            DataWrapper(
                Status.Success,
                result.body()
            )
        } else {
            DataWrapper(
                Status.Error(result.message()),
                result.body()
            )
        }
    }
}