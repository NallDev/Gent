package com.nalldev.gent.data.datasources.remote

import com.nalldev.gent.domain.models.EventResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteDatasource(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchEvent(active: Int, keyword: String? = null): EventResponse = withContext(
        ioDispatcher
    ) {
        apiService.getEvent(active, keyword)
    }
}