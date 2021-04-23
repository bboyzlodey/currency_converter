package com.example.currencyconverter.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.StringRes
import com.example.currencyconverter.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

sealed class NetworkStatus(val color: Int, @StringRes val message: Int) {
    object Available : NetworkStatus(Color.GREEN, R.string.network_status_available)
    object Unavailable : NetworkStatus(Color.RED, R.string.network_status_unavailable)
}

@ExperimentalCoroutinesApi
class NetworkStatusTracker(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkStatus: Flow<NetworkStatus>

    init {
        networkStatus = callbackFlow<NetworkStatus> {
            val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    Timber.i("Network connection is Lost")
                    offer(NetworkStatus.Unavailable)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Timber.i("Network connection unavailable")
                    offer(NetworkStatus.Unavailable)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Timber.i("Network connection available")
                    offer(NetworkStatus.Available)
                }
            }
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, networkStatusCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkStatusCallback)
            }
        }

    }
}

inline fun <Result> Flow<NetworkStatus>.map(
    crossinline onUnavailable: suspend () -> Result,
    crossinline onAvailable: suspend () -> Result
) : Flow<Result> = map{status ->
    when(status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}