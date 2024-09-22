package com.nalldev.gent.utils

import android.content.Context
import com.nalldev.gent.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

inline fun <reified T> (suspend () -> Response<T>).toFlowState(
    context: Context
): Flow<UIState<T>> {
    return flow {
        val isInternetConnected = RemoteHelper.hasInternetConnection(context)
        if (isInternetConnected) {
            emit(UIState.Loading)
            try {
                val response = this@toFlowState()
                if (response.isSuccessful && response.body() != null) {
                    emit(UIState.Success(response.body()!!))
                } else {
                    emit(UIState.Error(RemoteHelper.remoteErrorMessage(context, response.code())))
                }
            } catch (e: Exception) {
                emit(UIState.Error(context.getString(R.string.error_code_default)))
            }
        } else {
            emit(UIState.Error(context.getString(R.string.no_internet)))
        }
    }.flowOn(Dispatchers.IO)
}