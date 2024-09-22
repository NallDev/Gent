package com.nalldev.gent.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.nalldev.gent.R

object RemoteHelper {
    fun hasInternetConnection(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (e: Exception) {
            return false
        }
    }

    fun remoteErrorMessage(context: Context, code: Int): String {
        return when (code) {
            400 -> context.getString(R.string.error_code_400)
            401 -> context.getString(R.string.error_code_401)
            403 -> context.getString(R.string.error_code_403)
            404 -> context.getString(R.string.error_code_404)
            408 -> context.getString(R.string.error_code_408)
            429 -> context.getString(R.string.error_code_429)
            in 500..599 -> context.getString(R.string.error_code_500)
            else -> context.getString(R.string.error_code_default)
        }
    }
}