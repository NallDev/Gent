package com.nalldev.gent.di

import android.app.Application
import androidx.room.Room
import com.nalldev.gent.data.datasources.local.EventBookmarkDao
import com.nalldev.gent.data.datasources.local.EventDb
import com.nalldev.gent.data.datasources.local.EventDao
import org.koin.dsl.module

fun provideDatabase(application: Application): EventDb =
    Room.databaseBuilder(
        application,
        EventDb::class.java,
        "event_db"
    )
        .fallbackToDestructiveMigration()
        .build()

fun provideEventDao(eventDb: EventDb): EventDao = eventDb.getEventDao()

fun provideEventBookmarkDao(eventDb: EventDb): EventBookmarkDao = eventDb.getEventBookmarkDao()

val databaseModule= module {
    single { provideDatabase(get()) }
    single { provideEventDao(get()) }
    single { provideEventBookmarkDao(get()) }
}