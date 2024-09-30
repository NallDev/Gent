package com.nalldev.gent.data.datasources.remote

import com.nalldev.gent.domain.models.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("events")
    suspend fun getEvent(
        @Query("active") active: Int,
        @Query("q") keyword: String?,
    ) : EventResponse

    @GET("events")
    suspend fun getFirstActiveEvent(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 1
    ) : EventResponse
}