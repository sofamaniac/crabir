package com.sofamaniac.crabir

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

val Context.onWifiConnection: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.activeNetwork.let { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) ?: false
        }
    }
