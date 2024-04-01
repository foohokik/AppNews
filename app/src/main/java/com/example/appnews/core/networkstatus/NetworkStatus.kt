package com.example.appnews.core.networkstatus

sealed class NetworkStatus{
    data object Unknown: NetworkStatus()
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}
