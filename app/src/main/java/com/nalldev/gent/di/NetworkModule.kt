package com.nalldev.gent.di

import android.app.Application
import android.content.pm.ApplicationInfo
import com.nalldev.gent.data.datasources.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideInterceptor(application: Application) : HttpLoggingInterceptor {
    return if (0 != application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
}

fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()
}


fun provideConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()


fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://event-api.dicoding.dev/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)


val networkModule= module {
    single { provideInterceptor(get()) }
    single { provideHttpClient(get()) }
    single { provideConverterFactory() }
    single { provideRetrofit(get(),get()) }
    single { provideService(get()) }
}