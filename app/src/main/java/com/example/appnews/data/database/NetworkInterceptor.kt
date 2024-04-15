package com.example.appnews.data.database

import com.example.appnews.core.networkstatus.NetworkConnectivityService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val networkConnectivityService: NetworkConnectivityService
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        if (networkConnectivityService.isConnected()) {
            return chain.proceed(request)
        } else {
            throw NoNetworkException()
        }
    }

    class NoNetworkException: IOException("Please check Network Connection")
}
