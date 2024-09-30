package com.nalldev.gent.data.datasources.remote

import android.content.Context
import com.nalldev.gent.R
import com.nalldev.gent.domain.models.EventResponse
import com.nalldev.gent.utils.ApiException
import com.nalldev.gent.utils.NoInternetException
import com.nalldev.gent.utils.RemoteHelper
import com.nalldev.gent.utils.UnknownException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RemoteDatasource(
    private val apiService: ApiService,
    private val context : Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchEvent(active: Int, keyword: String? = null): EventResponse = withContext(
        ioDispatcher
    ) {
        try {
            if (!RemoteHelper.hasInternetConnection(context)) {
                throw NoInternetException(context.getString(R.string.no_internet))
            }
            val response = apiService.getEvent(active, keyword)
            if (response.error == true) {
                throw ApiException(response.message ?: context.getString(R.string.error_code_default))
            }
            response
        } catch (e: HttpException) {
            val code = e.code()
            val errorMessage = RemoteHelper.remoteErrorMessage(context, code)
            throw ApiException(errorMessage)
        } catch (e: IOException) {
            throw NoInternetException(context.getString(R.string.no_internet))
        } catch (e: Exception) {
            throw UnknownException(e.message ?: context.getString(R.string.error_code_default))
        }
    }
}