package com.example.product.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

//class NetworkStateListener @Inject constructor(@param:ApplicationContext private val context: Context) {
//
//    private val connectivityManager: ConnectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private var lastNetworkStatus: NetWorkStatus? = null
//
//    // This method creates a Flow that emits network status changes
//    fun getNetworkStatusFlow(): Flow<NetWorkStatus> = callbackFlow {
//        val networkCallback = object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                // Network is available
//                val newStatus = NetWorkStatus.NetworkAvailable
//                if (lastNetworkStatus != newStatus) {
//                    trySend(newStatus)
//                    lastNetworkStatus = newStatus
//                }
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                // Network is lost
//                val newStatus = NetWorkStatus.NetworkLost
//                if (lastNetworkStatus != newStatus) {
//                    trySend(newStatus)
//                    lastNetworkStatus = newStatus
//                }
//            }
//
//            override fun onCapabilitiesChanged(
//                network: Network,
//                networkCapabilities: NetworkCapabilities
//            ) {
//                super.onCapabilitiesChanged(network, networkCapabilities)
//
//                // Check if the network has internet capability
//                val newStatus = if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
//                    NetWorkStatus.NetworkHasInternet
//                } else {
//                    NetWorkStatus.NetworkHasNoInternet
//                }
//
//                if (lastNetworkStatus != newStatus) {
//                    trySend(newStatus)
//                    lastNetworkStatus = newStatus
//                }
//            }
//        }
//
//        // Register the network callback to monitor changes
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val networkRequest = android.net.NetworkRequest.Builder()
//                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                .build()
//            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//        }
//
//        // Cleanup when the Flow collector is no longer in use
//        awaitClose {
//            connectivityManager.unregisterNetworkCallback(networkCallback)
//        }
//    }
//}


class NetworkStateListener @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var lastNetworkStatus: NetWorkStatus? = null

    // This method creates a Flow that emits network status changes
    fun getNetworkStatusFlow(): Flow<NetWorkStatus> = callbackFlow {
        // Check the current network status at the beginning and pass the send function
        checkCurrentNetworkStatus { newStatus ->
            // Emit the status using trySend within callbackFlow
            trySend(newStatus)
        }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Network is available
                val newStatus = NetWorkStatus.NetworkAvailable
                if (lastNetworkStatus != newStatus) {
                    trySend(newStatus)
                    lastNetworkStatus = newStatus
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is lost
                val newStatus = NetWorkStatus.NetworkLost
                if (lastNetworkStatus != newStatus) {
                    trySend(newStatus)
                    lastNetworkStatus = newStatus
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                // Check if the network has internet capability
                val newStatus =
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        NetWorkStatus.NetworkHasInternet
                    } else {
                        NetWorkStatus.NetworkHasNoInternet
                    }

                if (lastNetworkStatus != newStatus) {
                    trySend(newStatus)
                    lastNetworkStatus = newStatus
                }
            }
        }

        // Register the network callback to monitor changes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkRequest = android.net.NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }

        // Cleanup when the Flow collector is no longer in use
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    // Function to check the current network status and pass the send function to emit the status
    private fun checkCurrentNetworkStatus(send: (NetWorkStatus) -> Unit) {
        // Check the current network status at the beginning
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        // Determine the current status and emit it
        val newStatus = when {
            activeNetwork == null -> NetWorkStatus.NetworkLost // No active network
            networkCapabilities == null -> NetWorkStatus.NetworkLost // No capabilities
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> NetWorkStatus.NetworkHasInternet
            else -> NetWorkStatus.NetworkHasNoInternet
        }

        // Emit the initial network status if it's different from the last known status
        if (lastNetworkStatus != newStatus) {
            send(newStatus)
            lastNetworkStatus = newStatus
        }
    }
}

enum class NetWorkStatus(val msg: String) {
    NetworkHasNoInternet("Network has no internet"),
    NetworkHasInternet("Network has internet"),
    NetworkLost("Network Lost"),
    NetworkAvailable("Network Available"),
}
