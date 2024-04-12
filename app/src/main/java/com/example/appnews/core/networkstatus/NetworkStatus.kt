package com.example.appnews.core.networkstatus

sealed class NetworkStatus{
    object Unknown: NetworkStatus()
    object Connected: NetworkStatus()
   object Disconnected: NetworkStatus()
}
