package com.example.appnews.core.networkstatus

import com.example.appnews.R
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {

    val networkStatus: Flow<NetworkStatus>
    fun isConnected():Boolean

}