package com.example.appnews.core

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HttpResultToDataWrapperConverter @Inject constructor() {

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