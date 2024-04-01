package com.example.appnews.data.database

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val networkConnectivityService: NetworkConnectivityService
) : Interceptor {


   // val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("CRASH", "interceptor before")
        val request: Request = chain.request()
        Log.d("CRASH", "interceptor after")
        if(networkConnectivityService.isConnected()){
            return chain.proceed(request)
        }else{
            Log.d("CRASH", "here before IOExc interceptor")
            throw NoNetworkException()
        }
    }

    class NoNetworkException internal constructor() : RuntimeException("Please check Network Connection")
}
