package com.example.translator.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

var isNetworkConnected = false

fun isOnline(context: Context) {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val builder: NetworkRequest.Builder = NetworkRequest.Builder()

    connectivityManager.registerNetworkCallback(
        builder.build(),
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                 isNetworkConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                isNetworkConnected = false
            }
        }
    )

}
