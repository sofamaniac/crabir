package com.sofamaniac.crabir

import android.content.Context
import android.net.ConnectivityManager


val Context.onWifiConnection: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.allNetworks.any { network ->
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                ?: false
        }
    }
